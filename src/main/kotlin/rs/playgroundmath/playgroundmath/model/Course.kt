package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.Date

@Entity
@Table(name = "course")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    val courseId: Long = 0,

    @Column(name = "age", nullable = false)
    val age: Long = 0,

    @Column(name = "due_date", nullable = true)
    val dueDate: LocalDateTime? = null,

    @Column(name = "title", nullable = false)
    val title: String = "",

    @Column(name = "description", nullable = false)
    val description: String = "",

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    val user: User? = null,

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val tests: List<Test> = emptyList()
)
