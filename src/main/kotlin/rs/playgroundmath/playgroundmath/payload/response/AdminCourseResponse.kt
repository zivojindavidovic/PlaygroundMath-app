package rs.playgroundmath.playgroundmath.payload.response

data class AdminCourseResponse(
    val courseId: Long,
    val courseName: String,
    val courseDescription: String,
    val courseAge: Long,
    val tests: List<AdminTestResponse>
)
