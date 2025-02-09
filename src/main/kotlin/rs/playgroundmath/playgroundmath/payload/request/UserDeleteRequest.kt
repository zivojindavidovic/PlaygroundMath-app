package rs.playgroundmath.playgroundmath.payload.request

data class UserDeleteRequest(
    val userId: Long,
    val password: String
)
