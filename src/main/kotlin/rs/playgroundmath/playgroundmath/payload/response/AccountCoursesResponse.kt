package rs.playgroundmath.playgroundmath.payload.response

data class AccountCoursesResponse(
    val accountId: Long,
    val username: String,
    val courses: List<CoursesResponse>
)
