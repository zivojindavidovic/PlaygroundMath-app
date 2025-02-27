package rs.playgroundmath.playgroundmath.payload.response

data class AdminTestResponse(
    val testId: Long,
    val tasks: List<AdminTaskResponse>?
)
