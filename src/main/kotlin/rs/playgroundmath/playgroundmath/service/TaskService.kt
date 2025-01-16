package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.model.TaskStatus
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.response.GenerateTasksResponse
import rs.playgroundmath.playgroundmath.payload.response.SolveTestResponse
import rs.playgroundmath.playgroundmath.payload.response.TaskResponse
import rs.playgroundmath.playgroundmath.repository.AccountRepository
import rs.playgroundmath.playgroundmath.repository.TaskRepository
import kotlin.random.Random

typealias ApplicationTask = rs.playgroundmath.playgroundmath.model.Task

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val accountRepository: AccountRepository
) {

    fun generateTasks(generateTasksRequest: GenerateTasksRequest): GenerateTasksResponse {
//        val tasks: MutableList<String> = mutableListOf()

        for (i in 1..50) {
            val firstNumber = Random.nextLong(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo)
            val secondNumber = Random.nextLong(generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo)

            val randomOperation = generateTasksRequest.operations.random()

            val task = "$firstNumber $randomOperation $secondNumber = "

            var result: Double = 0.0
            if (generateTasksRequest.testType == "online") {
                result = when (randomOperation) {
                    "+" -> (firstNumber + secondNumber).toDouble()
                    "-" -> (firstNumber - secondNumber).toDouble()
                    "*" -> (firstNumber * secondNumber).toDouble()
                    "/" -> if (secondNumber != 0L) (firstNumber / secondNumber).toDouble() else Double.NaN  // Avoid division by zero
                    else -> 0.0
                }

                saveTaskToDb(task, result, generateTasksRequest.accountId)
            }

//            tasks.add(task)
        }

        val last30Tasks = findLast30NotCompletedTasks()

        return GenerateTasksResponse(last30Tasks.map {
            it.toResponse()
        })
    }

    private fun Task.toResponse(): TaskResponse =
        TaskResponse(
            taskId = this.taskId,
            task = this.task
        )


    private fun findLast30NotCompletedTasks(): List<Task> =
        taskRepository.findTop30ByAccountAndStatusOrderByTaskIdDesc(5, TaskStatus.NO)

    fun solveTest(solveTestRequest: SolveTestRequest): SolveTestResponse {
        var pointsForTest: Long = 0
        for (answerMap in solveTestRequest.testAnswers) {
            for ((key, value) in answerMap) {
                val task = taskRepository.findById(key).orElseThrow{
                    IllegalArgumentException("Task not found with ID: $key")
                }

                if (task.isCompleted == TaskStatus.YES) {
                    continue
                }

                val updatedTask = if (task.result.toLong() == value.toLong()) {
                    pointsForTest += 3
                    task.copy(
                        points = task.points + 3,
                        isCompleted = TaskStatus.YES
                    )
                } else {
                    task.copy(
                        isCompleted = TaskStatus.YES
                    )
                }

                taskRepository.save(updatedTask)
            }
        }

        return SolveTestResponse(pointsFromTest = pointsForTest, totalPoints = getTotalPointsForAccount(solveTestRequest))
    }

    private fun getTotalPointsForAccount(solveTestRequest: SolveTestRequest): Long =
        taskRepository.sumPointsByAccountAndStatus(solveTestRequest.accountId, TaskStatus.YES)
            ?: 0


    private fun saveTaskToDb(task: String, result: Double, accountId: Long) {
        val account = accountRepository.findById(accountId).orElseThrow {
            IllegalArgumentException("Account not found with ID: $accountId")
        }

        taskRepository.save(ApplicationTask(account = account, task = task, result = result, points = 0))
    }
}
