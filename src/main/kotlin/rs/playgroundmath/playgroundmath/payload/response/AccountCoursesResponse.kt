package rs.playgroundmath.playgroundmath.payload.response

data class AccountCoursesResponse(
    val accountId: Long,
    val courses: List<CoursesResponse>
)
