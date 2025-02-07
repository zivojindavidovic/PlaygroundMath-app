package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*
import rs.playgroundmath.playgroundmath.enums.YesNo

@Entity
@Table(name = "test")
data class Test(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id", nullable = false)
    val testId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = true)
    val course: Course? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = true)
    val account: Account? = null,

    @Column(name = "is_completed", nullable = true, columnDefinition = "ENUM('YES', 'NO')")
    @Enumerated(EnumType.STRING)
    val isCompleted: YesNo? = null,

    @OneToMany(mappedBy = "test", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val tasks: List<Task>? = null
)
