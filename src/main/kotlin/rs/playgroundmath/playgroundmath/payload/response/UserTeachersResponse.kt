package rs.playgroundmath.playgroundmath.payload.response

data class UserTeachersResponse(
    val teacherId: Long,
    val teacherEmail: String,
    val numberOfActiveCourses: Int
)
