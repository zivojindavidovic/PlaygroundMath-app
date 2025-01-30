package rs.playgroundmath.playgroundmath.payload.response

data class CourseApplicationResponse(
    val courseId: Long,
    val courseAge: Long,
    val accountId: Long,
    val accountAge: Long,
    val accountUsername: String
)
