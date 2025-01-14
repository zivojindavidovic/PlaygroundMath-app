package rs.playgroundmath.playgroundmath.payload.response

data class AccountCreateResponse(
    val id: Long,
    val username: String,
    val userId: Long
)