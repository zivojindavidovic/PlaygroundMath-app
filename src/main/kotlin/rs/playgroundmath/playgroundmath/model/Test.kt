package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*

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
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account? = null,

//    @OneToMany(mappedBy = "test", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    val tasks: List<Task>? = null
)
