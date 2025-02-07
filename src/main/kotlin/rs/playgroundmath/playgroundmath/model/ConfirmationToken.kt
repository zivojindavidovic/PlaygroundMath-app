package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class ConfirmationToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val token: String = "",

    val userId: Long = 0,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val expiresAt: LocalDateTime = LocalDateTime.now(),

    var confirmedAt: LocalDateTime? = null
)
