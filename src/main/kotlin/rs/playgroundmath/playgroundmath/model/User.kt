package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,

    @Column(name = "email", nullable = false, length = 255, unique = true)
    val email: String = "",

    @Column(name = "password", nullable = false, length = 255)
    val password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "enum('active', 'pending', 'suspended', 'deleted')")
    val status: Status = Status.PENDING,

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)