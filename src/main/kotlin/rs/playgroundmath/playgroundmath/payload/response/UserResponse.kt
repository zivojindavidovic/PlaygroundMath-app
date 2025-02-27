package rs.playgroundmath.playgroundmath.payload.response

data class UserResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val accounts: List<AccountResponse>
)
