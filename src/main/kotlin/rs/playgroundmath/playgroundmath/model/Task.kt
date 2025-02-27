package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*

@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    val taskId: Long = 0,

    @Column(name = "first_number", nullable = false)
    val firstNumber: String = "",

    @Column(name = "second_number", nullable = false)
    val secondNumber: String = "",

    @Column(name = "operation", nullable = false)
    val operation: String = "",

    @Column(name = "result", nullable = false)
    val result: String = "",

    @Column(name = "points", nullable = false)
    val points: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    val test: Test? = null,

    @OneToMany(
        mappedBy = "task",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val accountTasks: List<AccountTask> = emptyList()
)