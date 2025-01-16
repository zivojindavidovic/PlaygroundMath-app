package rs.playgroundmath.playgroundmath.payload.request

data class UpdateAccountRequest(
    val accountId: Long,
    val username: String
)
