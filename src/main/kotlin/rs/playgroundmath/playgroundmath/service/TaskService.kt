package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.response.OnlineTaskResponse
import rs.playgroundmath.playgroundmath.payload.response.PdfTaskResponse
import rs.playgroundmath.playgroundmath.payload.response.SolveTestResponse
import rs.playgroundmath.playgroundmath.payload.response.TaskGetUnresolvedResponse
import rs.playgroundmath.playgroundmath.repository.AccountRepository
import rs.playgroundmath.playgroundmath.repository.TaskRepository
import rs.playgroundmath.playgroundmath.repository.TestRepository
import kotlin.random.Random

typealias ApplicationTask = rs.playgroundmath.playgroundmath.model.Task

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val accountRepository: AccountRepository,
    private val testRepository: TestRepository
) {

    fun generateTasks(generateTasksRequest: GenerateTasksRequest): Any? {
        return if (generateTasksRequest.testType == "pdf") {
            generatePdfTasks(generateTasksRequest)
        } else {
            generateOnlineTasks(generateTasksRequest)
        }
    }

    private fun generatePdfTasks(generateTasksRequest: GenerateTasksRequest): PdfTaskResponse {
        val tasks = mutableListOf<String>()
        for (i in 1..20) {
            val firstNumber = Random.nextLong(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo)
            val secondNumber = Random.nextLong(generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo)

            val randomOperation = generateTasksRequest.operations.random()

            val task = "$firstNumber $randomOperation $secondNumber = "

            tasks.add(task)
        }

        return PdfTaskResponse(tasks = tasks)
    }

    private fun generateOnlineTasks(generateTasksRequest: GenerateTasksRequest): Any? {
        if (countUnresolvedTestsByAccountId(generateTasksRequest.accountId) > 0) {
            return OnlineTaskResponse("Account already has unresolved test")
        }

        val account = accountRepository.findById(generateTasksRequest.accountId)
        val test = testRepository.save(Test(account = account.get(), isCompleted = YesNo.NO))

        for(i in 1..20) {
            val firstNumber = Random.nextLong(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo)
            val secondNumber = Random.nextLong(generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo)

            val randomOperation = generateTasksRequest.operations.random()

            val result = when (randomOperation) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> firstNumber / secondNumber
                else -> 0.0
            }

            val points = when (randomOperation) {
                "+" -> 1
                "-" -> 2
                "*" -> 3
                "/" -> 4
                else -> 1
            }

            taskRepository.save(Task(firstNumber = firstNumber.toString(), secondNumber = secondNumber.toString(), operation = randomOperation, result = result.toString(), points = points, test = test))
        }

        return OnlineTaskResponse("Test created")
    }

    private fun countUnresolvedTestsByAccountId(accountId: Long): Long =
        testRepository.countByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)

    fun getUnresolvedTestByAccountId(accountId: Long): Any? {
        val hasUnresolvedTest = countUnresolvedTestsByAccountId(accountId) > 0

        if (hasUnresolvedTest) {
            val unresolvedTest = testRepository.findByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)

            val tasks = taskRepository.findAllByTest(unresolvedTest)

            return TaskGetUnresolvedResponse(tasks = tasks.map {
                it.toTaskResponse()
            })
        }

        return TaskGetUnresolvedResponse()
    }

    private fun ApplicationTask.toTaskResponse(): rs.playgroundmath.playgroundmath.payload.response.TaskResponse =
        rs.playgroundmath.playgroundmath.payload.response.TaskResponse(
            taskId = this.taskId,
            task = this.firstNumber + " " + this.operation + " " + this.secondNumber
        )

    fun solveTest(solveTestRequest: SolveTestRequest): SolveTestResponse {
        var points = 0
        val unresolvedTest = testRepository.findByAccount_AccountIdAndIsCompleted(solveTestRequest.accountId, YesNo.NO)
        val tasks = taskRepository.findAllByTest(unresolvedTest)
        tasks.forEach() {
            val currentTaskId = it.taskId
            val userAnswer = solveTestRequest.testAnswers.firstNotNullOfOrNull { it[currentTaskId] }

            if (userAnswer!!.toLong() == it.result.toLong()) {
                points += it.points
            }
        }
        val account = accountRepository.findById(solveTestRequest.accountId).get()
        val updatedAccount = account.copy(points = account.points + points)

        accountRepository.save(updatedAccount)

        val updatedTest = unresolvedTest.copy(isCompleted = YesNo.YES)
        testRepository.save(updatedTest)

        return SolveTestResponse(pointsFromTest = points.toLong())
    }
}
