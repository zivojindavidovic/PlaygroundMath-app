package rs.playgroundmath.playgroundmath.payload.request

data class UserRegisterRequest(
    val email: String,
    val password: String
)