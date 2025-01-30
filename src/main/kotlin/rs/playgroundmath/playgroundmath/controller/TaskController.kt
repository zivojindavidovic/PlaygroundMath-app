package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.request.TaskGetUnresolvedRequest
import rs.playgroundmath.playgroundmath.payload.response.GenerateTasksResponse
import rs.playgroundmath.playgroundmath.payload.response.SolveTestResponse
import rs.playgroundmath.playgroundmath.service.TaskService

@RestController
@RequestMapping("/api/v1/task")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    fun generateTasks(@RequestBody generateTasksRequest: GenerateTasksRequest): Any? {
        return taskService.generateTasks(generateTasksRequest)
    }

    @PostMapping("/get")
    fun getAccountRelatedTest(@RequestBody taskGetUnresolvedRequest: TaskGetUnresolvedRequest): Any? {
        return taskService.getUnresolvedTestByAccountId(taskGetUnresolvedRequest.accountId)
    }
//
//    @PostMapping("/solve")
//    fun solveTest(@RequestBody solveTestRequest: SolveTestRequest): SolveTestResponse {
//        return taskService.solveTest(solveTestRequest)
//    }
}