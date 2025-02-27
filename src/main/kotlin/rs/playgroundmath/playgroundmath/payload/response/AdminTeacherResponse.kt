package rs.playgroundmath.playgroundmath.payload.response

data class AdminTeacherResponse(
    val teacherId: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val courses: List<AdminCourseResponse>
)
