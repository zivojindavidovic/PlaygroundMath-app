package rs.playgroundmath.playgroundmath.payload.response

data class GenerateTasksResponse(
    val tasks: List<TaskResponse> = emptyList()
)