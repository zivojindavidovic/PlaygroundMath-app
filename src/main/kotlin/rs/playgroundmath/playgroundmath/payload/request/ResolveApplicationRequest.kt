package rs.playgroundmath.playgroundmath.payload.request

data class ResolveApplicationRequest(
    val courseId: Long,
    val accountId: Long,
    val decision: Boolean
)
