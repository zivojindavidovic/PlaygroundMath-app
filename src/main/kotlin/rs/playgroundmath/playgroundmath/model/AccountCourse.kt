package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus

@Entity
@Table(name = "account_course")
data class AccountCourse(
    @EmbeddedId
    val id: AccountCourseId = AccountCourseId(),

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account? = null,

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    val course: Course? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: AccountCourseStatus = AccountCourseStatus.PENDING
)
