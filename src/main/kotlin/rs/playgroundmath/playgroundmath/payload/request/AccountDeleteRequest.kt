package rs.playgroundmath.playgroundmath.payload.request

data class AccountDeleteRequest(
    val accountId: Long,
    val userPassword: String
)
