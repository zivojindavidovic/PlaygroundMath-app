package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*

@Entity
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    val accountId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @Column(name = "username", nullable = false)
    val username: String = "",

    @Column(name = "points", nullable = false)
    val points: Long = 0
)
