package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.exceptions.TaskUserHasUnresolvedException
import rs.playgroundmath.playgroundmath.model.*
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.repository.AccountCourseRepository
import rs.playgroundmath.playgroundmath.repository.AccountCourseTestRepository
import rs.playgroundmath.playgroundmath.repository.AccountTaskRepository
import rs.playgroundmath.playgroundmath.repository.TaskRepository
import kotlin.math.floor
import kotlin.random.Random

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val accountService: AccountService,
    private val courseService: CourseService,
    private val testService: TestService,
    private val accountCourseTestRepository: AccountCourseTestRepository,
    private val accountTaskRepository: AccountTaskRepository,
    private val accountCourseRepository: AccountCourseRepository
): TaskService {

    override fun generateTasks(generateTasksRequest: GenerateTasksRequest): TaskGenerateResponse {
        val operations = generateTasksRequest.operations
        val accountId: Long? = generateTasksRequest.accountId
        val courseId: Long? = generateTasksRequest.courseId

        if (accountId != null && courseId == null) {
            val allowedOperationsConfig = mapOf(
                "+" to 0,
                "-" to 70,
                "*" to 200,
                "/" to 500
            )

            val userPoints = accountService.getAccountPoints(accountId)

            allowedOperationsConfig.forEach{
                if (operations.contains(it.key) && userPoints < it.value) {
                    operations.remove(it.key)
                }
            }
        }

        return if (generateTasksRequest.testType == "pdf" || generateTasksRequest.testType == "coursePdf") {
            generatePdfTasks(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo, generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo, operations, generateTasksRequest)
        } else {
            generateOnlineTasks(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo, generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo, operations, accountId, courseId, generateTasksRequest)
        }
    }

    override fun getUnresolvedTestByAccountId(accountId: Long): TaskUnresolvedTestResponse {
        val hasUnresolvedTest = testService.countUnresolvedTestsByAccountId(accountId) > 0

        if (hasUnresolvedTest) {
            val unresolvedTest = testService.findUnresolvedTestsByAccountId(accountId)

            val tasks = taskRepository.findAllByTest(unresolvedTest)

            return TaskUnresolvedTestResponse(tasks = tasks.map {
                it.toTaskResponse()
            })
        }

        return TaskUnresolvedTestResponse()
    }

    override fun solveTest(solveTestRequest: SolveTestRequest): SolveTestResponse {
        var unresolvedTest: Test? = null

        unresolvedTest = if (solveTestRequest.testId != null) {
            testService.findByTestId(solveTestRequest.testId)
        } else {
            testService.findUnresolvedTestsByAccountId(solveTestRequest.accountId)
        }

        val tasks = taskRepository.findAllByTest(unresolvedTest)

        if (unresolvedTest.course != null) {
            return solveCourseTest(solveTestRequest, unresolvedTest, tasks)
        }

        val (points, updatedAccount) = computePointsAndUpdateAccount(solveTestRequest, tasks)

        val updatedTest = unresolvedTest.copy(isCompleted = YesNo.YES)
        testService.saveTest(updatedTest)

        return SolveTestResponse(
            pointsFromTest = points.toLong(),
            totalPoints = updatedAccount.points
        )
    }

    private fun solveCourseTest(
        solveTestRequest: SolveTestRequest,
        unresolvedTest: Test,
        tasks: List<Task>
    ): SolveTestResponse {
        val (points, updatedAccount) = computePointsAndUpdateAccount(solveTestRequest, tasks, true)

        val accountCourseTest = accountCourseTestRepository
            .findByAccount_AccountIdAndTest_TestId(
                solveTestRequest.accountId,
                unresolvedTest.testId
            )

        val updatedAccountCourseTest = accountCourseTest.copy(
            wonPoints = points,
            passed = YesNo.YES,
            isCompleted = YesNo.YES
        )

        accountCourseTestRepository.save(updatedAccountCourseTest)

        return SolveTestResponse(
            pointsFromTest = points.toLong(),
            totalPoints = updatedAccount.points
        )
    }

    private fun computePointsAndUpdateAccount(
        solveTestRequest: SolveTestRequest,
        tasks: List<Task>,
        isCourseTest: Boolean = false
    ): Pair<Int, Account> {
        var points = 0

        val account = accountService.findByAccountId(solveTestRequest.accountId)

        tasks.forEach { task ->
            val currentTaskId = task.taskId
            val userAnswer = solveTestRequest.testAnswers.firstNotNullOfOrNull { it[currentTaskId] }
            if (userAnswer!!.toLong() == task.result.toLong()) {
                points += task.points
            }

            if (isCourseTest) {
                val accountTask = accountTaskRepository.findAllByAccount_AccountIdAndTask_TaskId(accountId = account.accountId, taskId = task.taskId)

                val updatedAccountTask = accountTask.copy(
                    answer = userAnswer
                )

                accountTaskRepository.save(updatedAccountTask)
            }
        }

        val updatedAccount = account.copy(points = account.points + points)
        accountService.saveAccount(updatedAccount)

        return points to updatedAccount
    }


    private fun generatePdfTasks(numberOneFrom: Long, numberOneTo: Long, numberTwoFrom: Long, numberTwoTo: Long, operations: MutableList<String>, request: GenerateTasksRequest): TaskGenerateResponse {
        val tasks = mutableListOf<String>()

        for (i in 1..20) {
            val taskInformation = generateRuleBasedTasks(numberOneFrom, numberOneTo, numberTwoFrom, numberTwoTo, operations, request)
            val task = "${taskInformation["firstNumber"]} ${taskInformation["operation"]} ${taskInformation["secondNumber"]} = "

            tasks.add(task)
        }

        return TaskGenerateResponse(
            tasks = tasks
        )
    }

    private fun generateOnlineTasks(numberOneFrom: Long, numberOneTo: Long, numberTwoFrom: Long, numberTwoTo: Long, operations: MutableList<String>, accountId: Long?, courseId: Long?, request: GenerateTasksRequest): TaskGenerateResponse {
        var test: Test? = null
        var account: Account? = null

        if (accountId != null) {
            val unresolvedTasks = testService.countUnresolvedTestsByAccountId(accountId)
            if (unresolvedTasks > 0) {
                throw TaskUserHasUnresolvedException("Već imaš generisane zadatke koje nisi rešio")
            }

            val account = accountService.findByAccountId(accountId)
            test = testService.saveTest(Test(account = account, isCompleted = YesNo.NO))
        }

        if (courseId != null) {
            val course = courseService.findByCourseId(courseId)
            test = testService.saveTest(Test(course = course))
        }

        var possiblePoints = 0
        for(i in 1..20) {
            val taskInformation = generateRuleBasedTasks(numberOneFrom, numberOneTo, numberTwoFrom, numberTwoTo, operations, request)

            val points = when (taskInformation["operation"]) {
                "+" -> 1
                "-" -> 2
                "*" -> 3
                "/" -> 4
                else -> 1
            }

            possiblePoints += points

            val task = Task(firstNumber = taskInformation["firstNumber"].toString(), secondNumber = taskInformation["secondNumber"].toString(), operation = taskInformation["operation"].toString(), result = taskInformation["result"].toString(), points = points, test = test)

            taskRepository.save(task)

            if (courseId != null) {
                val accountCourses = accountCourseRepository.findAllByCourse_CourseIdAndStatus(courseId, AccountCourseStatus.ACCEPTED)

                accountCourses.forEach { accountCourse ->
                    account = accountCourse.account

                    val accountTask = AccountTask(
                        account = account,
                        task = task,
                        result = taskInformation["result"].toString(),
                        answer = null
                    )

                    accountTaskRepository.save(accountTask)
                }
            }
        }

        if (courseId != null) {
            accountCourseTestRepository.save(AccountCourseTest(account = account, test = test, possiblePoints = possiblePoints, wonPoints = 0, passed = YesNo.NO, isCompleted = YesNo.NO))
        }

        return TaskGenerateResponse(type = "online")
    }

    private fun generateRuleBasedTasks(
        numberOneFrom: Long,
        numberOneTo: Long,
        numberTwoFrom: Long,
        numberTwoTo: Long,
        operations: MutableList<String>,
        request: GenerateTasksRequest
    ): Map<String, Any> {
        val operation = operations.random()

        val firstNumber = Random.nextLong(numberOneFrom, numberOneTo)
        val secondNumber = generateSecondNumberBasedOnFirstNumber(firstNumber, numberTwoFrom, numberTwoTo, operation, request)

        val result = when (operation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> firstNumber / secondNumber
            else -> 0.0
        }

        return mapOf(
            "firstNumber" to firstNumber,
            "secondNumber" to secondNumber,
            "operation" to operation,
            "result" to result
        )
    }

    private fun generateSecondNumberBasedOnFirstNumber(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long, operations: String, request: GenerateTasksRequest): Long {
        return when (operations) {
            "+" -> generateSecondNumberForSum(firstNumber, numberTwoFrom, numberTwoTo, request.sumUnitsGoesOverCurrentTenSum, request.sumExceedTwoDigitsSum)
            "-" -> generateSecondNumberForSub(firstNumber, numberTwoFrom, numberTwoTo, request.allowedNegativeResultsSub, request.allowedBiggerUnitsInSecondNumberSub)
            "*" -> generateSecondNumberForMul(firstNumber, numberTwoFrom, numberTwoTo, request.allowedThreeDigitsResultMul)
            "/" -> generateSecondNumberForDiv(firstNumber, numberTwoFrom, numberTwoTo)
            else -> {1}
        }
    }

    private fun generateSecondNumberForSum(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long, sumUnitsGoesOverCurrentTen: Boolean, sumExceedTwoDigits: Boolean): Long {
        val candidatesNoUnitsCarry = if (!sumUnitsGoesOverCurrentTen) {
            (numberTwoFrom..numberTwoTo).filter { second ->
                ((firstNumber % 10) + (second % 10)) < 10
            }
        } else {
            (numberTwoFrom..numberTwoTo).toList()
        }

        val candidatesNoThreeDigits = if (!sumExceedTwoDigits) {
            candidatesNoUnitsCarry.filter { second ->
                (firstNumber + second) < 100
            }
        } else {
            candidatesNoUnitsCarry
        }

        if (candidatesNoThreeDigits.isEmpty()) {
            return 0
        }

        return candidatesNoThreeDigits.random()
    }

    private fun generateSecondNumberForSub(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long, allowedNegativeResults: Boolean, allowedBiggerUnitsInSecondNumber: Boolean): Long {
        val range = numberTwoFrom..numberTwoTo

        val validCandidates = range.filter { second ->
            val noNegativeCondition = if (!allowedNegativeResults) {
                second < firstNumber
            } else {
                true
            }

            val noBiggerUnitsCondition = if (!allowedBiggerUnitsInSecondNumber) {
                (second % 10) <= (firstNumber % 10)
            } else {
                true
            }

            noNegativeCondition && noBiggerUnitsCondition
        }

        if (validCandidates.isEmpty()) {
            return 0
        }

        return validCandidates.random()
    }

    private fun generateSecondNumberForMul(
        firstNumber: Long,
        numberTwoFrom: Long,
        numberTwoTo: Long,
        allowedThreeDigitsResult: Boolean = false
    ): Long {
        var range = numberTwoFrom..numberTwoTo

        return if (allowedThreeDigitsResult) {
            range.random()
        } else {
            val maxPossibleResult = 99.0

            val div = maxPossibleResult / firstNumber
            val maxPossibleSecondNumber = floor(div).toInt()

            range = numberTwoFrom .. maxPossibleSecondNumber

            if (range.isEmpty()) {
                return 0
            }

            val newRange = numberTwoFrom..maxPossibleSecondNumber
            newRange.random()
        }
    }

    private fun generateSecondNumberForDiv(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long,): Long {
        val range = numberTwoFrom..numberTwoTo

        val candidatesForDivision = range.filter { second ->
            second != 0L && firstNumber % second == 0L
        }

        if (candidatesForDivision.isEmpty()) {
            return firstNumber
        }

        return candidatesForDivision.random()
    }

    private fun Task.toTaskResponse(): TaskResponse =
        TaskResponse(
            taskId = this.taskId,
            task = this.firstNumber + " " + this.operation + " " + this.secondNumber + " = "
        )
}
