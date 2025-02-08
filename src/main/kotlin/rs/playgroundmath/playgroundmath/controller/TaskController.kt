package rs.playgroundmath.playgroundmath.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.request.working.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.SolveTestResponse
import rs.playgroundmath.playgroundmath.payload.response.TaskGenerateResponse
import rs.playgroundmath.playgroundmath.payload.response.TaskUnresolvedTestResponse
import rs.playgroundmath.playgroundmath.service.TaskService

@RestController
@RequestMapping("/api/v1/task")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping("/generate")
    fun generateTasks(@RequestBody generateTasksRequest: GenerateTasksRequest): ResponseEntity<ApiResponse<TaskGenerateResponse>> {
        val results = taskService.generateTasks(generateTasksRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(results)
            )
        )
    }

    @GetMapping("/unresolved")
    fun getUnresolvedTest(@RequestParam("accountId") accountId: Long): ResponseEntity<ApiResponse<TaskUnresolvedTestResponse>> {
        val results = taskService.getUnresolvedTestByAccountId(accountId)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(results)
            )
        )
    }

    @PostMapping("/solve")
    fun solveTest(@RequestBody solveTestRequest: SolveTestRequest): ResponseEntity<ApiResponse<SolveTestResponse>> {
        val results = taskService.solveTest(solveTestRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(results)
            )
        )
    }
}