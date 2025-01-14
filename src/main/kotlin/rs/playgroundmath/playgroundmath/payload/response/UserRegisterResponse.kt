package rs.playgroundmath.playgroundmath.payload.response

import java.time.LocalDateTime

data class UserRegisterResponse(
    val id: Long,
    val email: String,
    val createdAt: LocalDateTime
)