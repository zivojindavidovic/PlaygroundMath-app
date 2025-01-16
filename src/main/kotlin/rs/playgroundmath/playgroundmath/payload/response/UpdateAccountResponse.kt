package rs.playgroundmath.playgroundmath.payload.response

data class UpdateAccountResponse(
    val accountId: Long,
    val username: String,
    val points: Long
)