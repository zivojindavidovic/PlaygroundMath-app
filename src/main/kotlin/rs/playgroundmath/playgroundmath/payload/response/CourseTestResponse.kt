package rs.playgroundmath.playgroundmath.payload.response

data class CourseTestResponse(
    val testId: Long,
    val tasks: List<CourseTaskResponse> = emptyList()
)
