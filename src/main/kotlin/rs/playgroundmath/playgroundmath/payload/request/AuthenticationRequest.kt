package rs.playgroundmath.playgroundmath.payload.request

data class AuthenticationRequest(
    val email: String,
    val password: String
)
