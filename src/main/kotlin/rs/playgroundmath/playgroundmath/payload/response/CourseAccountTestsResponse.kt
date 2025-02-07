package rs.playgroundmath.playgroundmath.payload.response

data class CourseAccountTestsResponse(
    val courseId: Long,
    val tests: List<CourseTestResponse> = emptyList()
)
