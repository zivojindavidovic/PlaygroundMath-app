package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.response.GenerateTasksResponse
import rs.playgroundmath.playgroundmath.service.TaskService

@RestController
@RequestMapping("/api/v1/task")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    fun generateTasks(@RequestBody generateTasksRequest: GenerateTasksRequest): GenerateTasksResponse {
        return taskService.generateTasks(generateTasksRequest)
    }
}