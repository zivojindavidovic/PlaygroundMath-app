package rs.playgroundmath.playgroundmath.payload.response

data class AccountRankListResponse(
    val accountId: Long,
    val username: String,
    val points: Long
)
