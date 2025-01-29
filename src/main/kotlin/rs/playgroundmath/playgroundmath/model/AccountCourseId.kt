package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class AccountCourseId(
    @Column(name = "account_id")
    val accountId: Long = 0,

    @Column(name = "course_id")
    val courseId: Long = 0
) : Serializable
