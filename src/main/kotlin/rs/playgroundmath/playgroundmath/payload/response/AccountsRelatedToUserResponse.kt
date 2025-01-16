package rs.playgroundmath.playgroundmath.payload.response

data class AccountResponse(
    val accountId: Long,
    val username: String,
    val points: Long
)

data class AccountRelatedToUserResponse(
    val accounts: List<AccountResponse>
)