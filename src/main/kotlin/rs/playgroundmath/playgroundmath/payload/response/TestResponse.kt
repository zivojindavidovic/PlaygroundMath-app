package rs.playgroundmath.playgroundmath.payload.response

data class TestResponse(
    val testId: Long,
    val isCompleted: Boolean,
    val tasks: List<TaskResponse>
)
