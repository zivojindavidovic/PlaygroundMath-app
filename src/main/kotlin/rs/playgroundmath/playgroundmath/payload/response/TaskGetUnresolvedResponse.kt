package rs.playgroundmath.playgroundmath.payload.response

data class TaskGetUnresolvedResponse(
    val type: String = "online",
    val tasks: List<TaskResponse> = emptyList()
)