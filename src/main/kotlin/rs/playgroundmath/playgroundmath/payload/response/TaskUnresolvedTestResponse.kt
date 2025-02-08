package rs.playgroundmath.playgroundmath.payload.response

data class TaskUnresolvedTestResponse(
    val type: String = "online",
    val tasks: List<TaskResponse> = emptyList()
)