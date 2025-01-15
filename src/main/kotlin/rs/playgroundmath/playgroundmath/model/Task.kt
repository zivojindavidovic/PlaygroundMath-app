package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*

@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    val taskId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account? = null,

    @Column(name = "task", nullable = false)
    val task: String = "",

    @Column(name = "result", nullable = false)
    val result: Double = 0.0,

    @Column(name = "points", nullable = false)
    val points: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "is_completed", nullable = false)
    val isCompleted: TaskStatus = TaskStatus.NO
)