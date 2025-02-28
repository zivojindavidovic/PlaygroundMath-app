package rs.playgroundmath.playgroundmath.payload.response

data class AdminUserResponse(
    val id: Long,
    val email: String,
    val isParent: Boolean,
    val isTeacher: Boolean,
    val firstName: String,
    val lastName: String
)