package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.exceptions.TaskUserHasUnresolvedException
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.repository.TaskRepository
import kotlin.math.floor
import kotlin.random.Random

typealias ApplicationTask = rs.playgroundmath.playgroundmath.model.Task

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val accountService: AccountService,
    private val courseService: CourseService,
    private val testService: TestService
) {

    fun generateTasks(generateTasksRequest: GenerateTasksRequest): TaskGenerateResponse {
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
            generatePdfTasks(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo, generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo, operations)
        } else {
            generateOnlineTasks(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo, generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo, operations, accountId, courseId)
        }
    }

    private fun generatePdfTasks(numberOneFrom: Long, numberOneTo: Long, numberTwoFrom: Long, numberTwoTo: Long, operations: MutableList<String>): TaskGenerateResponse {
        val tasks = mutableListOf<String>()

        for (i in 1..20) {
            val taskInformation = generateRuleBasedTasks(numberOneFrom, numberOneTo, numberTwoFrom, numberTwoTo, operations)
            val task = "${taskInformation["firstNumber"]} ${taskInformation["operation"]} ${taskInformation["secondNumber"]} = "

            tasks.add(task)
        }

        return TaskGenerateResponse(
            tasks = tasks
        )
    }

    private fun generateOnlineTasks(numberOneFrom: Long, numberOneTo: Long, numberTwoFrom: Long, numberTwoTo: Long, operations: MutableList<String>, accountId: Long?, courseId: Long?): TaskGenerateResponse {
        var test: Test? = null

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

        for(i in 1..20) {
            val taskInformation = generateRuleBasedTasks(numberOneFrom, numberOneTo, numberTwoFrom, numberTwoTo, operations)

            val points = when (taskInformation["operation"]) {
                "+" -> 1
                "-" -> 2
                "*" -> 3
                "/" -> 4
                else -> 1
            }

            taskRepository.save(Task(firstNumber = taskInformation["firstNumber"].toString(), secondNumber = taskInformation["secondNumber"].toString(), operation = taskInformation["operation"].toString(), result = taskInformation["result"].toString(), points = points, test = test))
        }

        return TaskGenerateResponse(type = "online")
    }

    private fun generateRuleBasedTasks(
        numberOneFrom: Long,
        numberOneTo: Long,
        numberTwoFrom: Long,
        numberTwoTo: Long,
        operations: MutableList<String>
    ): Map<String, Any> {
        val operation = operations.random()

        val firstNumber = Random.nextLong(numberOneFrom, numberOneTo)
        val secondNumber = generateSecondNumberBasedOnFirstNumber(firstNumber, numberTwoFrom, numberTwoTo, operation)

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

    private fun generateSecondNumberBasedOnFirstNumber(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long, operations: String): Long {
        return when (operations) {
            "+" -> generateSecondNumberForSum(firstNumber, numberTwoFrom, numberTwoTo)
            "-" -> generateSecondNumberForSub(firstNumber, numberTwoFrom, numberTwoTo)
            "*" -> generateSecondNumberForMul(firstNumber, numberTwoFrom, numberTwoTo)
            "/" -> generateSecondNumberForDiv(firstNumber, numberTwoFrom, numberTwoTo)
            else -> {1}
        }
    }

    private fun generateSecondNumberForSum(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long,): Long {
        val sumUnitsKeepCurrentTen = true
        val sumExceedTwoDigits = false

        val candidatesNoUnitsCarry = if (sumUnitsKeepCurrentTen) {
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

    private fun generateSecondNumberForSub(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long,): Long {
        val allowedNegativeResults = true
        val allowedBiggerUnitsInSecondNumber = true

        val range = numberTwoFrom..numberTwoTo

        val validCandidates = range.filter { second ->
            val noNegativeCondition = if (!allowedNegativeResults) {
                second <= firstNumber
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

    private fun generateSecondNumberForMul(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long,): Long {
        val allowedThreeDigitsResult = true

        val range = numberTwoFrom..numberTwoTo

        return if (allowedThreeDigitsResult) {
            range.random()
        } else {
            val maxPossibleResult = 99.0

            val div = maxPossibleResult / firstNumber
            val maxPossibleSecondNumber = floor(div).toInt()

            if (maxPossibleSecondNumber > numberTwoFrom) {
                return 0
            }

            val newRange = numberTwoFrom..maxPossibleSecondNumber
            newRange.random()
        }
    }

    private fun generateSecondNumberForDiv(firstNumber: Long, numberTwoFrom: Long, numberTwoTo: Long,): Long {
        val range = numberTwoFrom..numberTwoTo

        val candidatesForDivision = range.filter { second ->
            val result = firstNumber.toDouble() / second.toDouble()
            (result % 1) == 0.0
        }

        if (candidatesForDivision.isEmpty()) {
            return firstNumber
        }

        return candidatesForDivision.random()
    }

    fun getUnresolvedTestByAccountId(accountId: Long): Any? {
        val hasUnresolvedTest = testService.countUnresolvedTestsByAccountId(accountId) > 0

        if (hasUnresolvedTest) {
            val unresolvedTest = testService.findUnresolvedTestsByAccountId(accountId)

            val tasks = taskRepository.findAllByTest(unresolvedTest)

            return TaskGetUnresolvedResponse(tasks = tasks.map {
                it.toTaskResponse()
            })
        }

        return TaskGetUnresolvedResponse()
    }

    private fun ApplicationTask.toTaskResponse(): TaskResponse =
        TaskResponse(
            taskId = this.taskId,
            task = this.firstNumber + " " + this.operation + " " + this.secondNumber
        )

    fun solveTest(solveTestRequest: SolveTestRequest): SolveTestResponse {
        var points = 0
        val unresolvedTest = testService.findUnresolvedTestsByAccountId(solveTestRequest.accountId)
        val tasks = taskRepository.findAllByTest(unresolvedTest)
        tasks.forEach() {
            val currentTaskId = it.taskId
            val userAnswer = solveTestRequest.testAnswers.firstNotNullOfOrNull { it[currentTaskId] }

            if (userAnswer!!.toLong() == it.result.toLong()) {
                points += it.points
            }
        }

        val account = accountService.findByAccountId(solveTestRequest.accountId)
        val updatedAccount = account.copy(points = account.points + points)

        accountService.saveAccount(updatedAccount)

        val updatedTest = unresolvedTest.copy(isCompleted = YesNo.YES)
        testService.saveTest(updatedTest)

        return SolveTestResponse(pointsFromTest = points.toLong())
    }
}
