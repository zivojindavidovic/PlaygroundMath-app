package rs.playgroundmath.playgroundmath.payload.response

data class CoursesResponse(
    val courseId: Long,
    val courseTestsCount: Long,
    val accountSolvedTestsCount: Long
)