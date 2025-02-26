package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.enums.Status

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
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ACTIVE', 'PENDING', 'SUSPENDED', 'DELETED')")
    val status: Status = Status.PENDING,

    @Column(name = "first_name", nullable = false, length = 255)
    val firstName: String = "",

    @Column(name = "last_name", nullable = false, length = 255)
    val lastName: String = "",

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    val role: Role = Role(roleId = 2, roleType = RoleType.PARENT),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val accounts: List<Account> = listOf()
)