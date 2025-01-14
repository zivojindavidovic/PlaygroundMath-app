package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.response.GenerateTasksResponse
import kotlin.random.Random

@Service
class TaskService {

    fun generateTasks(generateTasksRequest: GenerateTasksRequest): GenerateTasksResponse {
        val tasks: MutableList<String> = mutableListOf()

        for (i in 1..50) {
            val firstNumber = Random.nextLong(generateTasksRequest.numberOneFrom, generateTasksRequest.numberOneTo)
            val secondNumber = Random.nextLong(generateTasksRequest.numberTwoFrom, generateTasksRequest.numberTwoTo)

            val randomOperation = generateTasksRequest.operations.random()

            val task = "$firstNumber $randomOperation $secondNumber = "

            tasks.add(task)
        }

        val tsk = tasks

        return GenerateTasksResponse(tasks)
    }
}
