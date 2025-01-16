package rs.playgroundmath.playgroundmath.payload.response

data class AuthenticationResponse(
    val accessToken: String,
    val userId: Long = 0,
    val email: String = ""
)
