package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.payload.request.GenerateTasksRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.response.SolveTestResponse
import rs.playgroundmath.playgroundmath.payload.response.TaskGenerateResponse
import rs.playgroundmath.playgroundmath.payload.response.TaskUnresolvedTestResponse

interface TaskService {

    fun generateTasks(generateTasksRequest: GenerateTasksRequest): TaskGenerateResponse

    fun getUnresolvedTestByAccountId(accountId: Long): TaskUnresolvedTestResponse

    fun solveTest(solveTestRequest: SolveTestRequest): SolveTestResponse
}