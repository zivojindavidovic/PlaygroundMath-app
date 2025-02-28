package rs.playgroundmath.playgroundmath.payload.response

data class AdminAccountResponse(
    val accountId: Long,
    val username: String,
    val points: Long,
    val age: Long
)
