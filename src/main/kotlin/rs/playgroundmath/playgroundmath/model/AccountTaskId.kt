package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class AccountTaskId(
    @Column(name = "account_id")
    val accountId: Long = 0,

    @Column(name = "task_id")
    val taskId: Long = 0
): Serializable
