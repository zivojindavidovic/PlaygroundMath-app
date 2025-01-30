package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*

@Entity
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    val accountId: Long = 0,

    @Column(name = "username", nullable = false)
    val username: String = "",

    @Column(name = "age", nullable = false)
    val age: Long = 0,

    @Column(name = "points", nullable = false)
    val points: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val tests: MutableList<Test> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "account_course",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    val courses: MutableList<Course> = mutableListOf()
)
