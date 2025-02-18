package rs.playgroundmath.playgroundmath.payload.request

data class AdminUpdateAccountPointsRequest(
    val accountId: Long,
    val points: Long
)
