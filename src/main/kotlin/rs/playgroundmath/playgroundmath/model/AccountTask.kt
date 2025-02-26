package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity
@Table(name = "account_task")
data class AccountTask(

    @EmbeddedId
    val id: AccountTaskId = AccountTaskId(),

    @Column(name = "result")
    var result: String? = null,

    @Column(name = "answer")
    val answer: String? = null,

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    val account: Account? = null,

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    val task: Task? = null
)
