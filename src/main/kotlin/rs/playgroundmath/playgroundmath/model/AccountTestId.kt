package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class AccountTestId(
    @Column(name = "account_id")
    val accountId: Long = 0,

    @Column(name = "test_id")
    val testId: Long = 0
) : Serializable