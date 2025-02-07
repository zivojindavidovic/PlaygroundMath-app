package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.*
import rs.playgroundmath.playgroundmath.enums.YesNo

@Entity
@Table(name = "account_course_test")
data class AccountCourseTest(
    @EmbeddedId
    val id: AccountCourseTestId = AccountCourseTestId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testId")
    @JoinColumn(name = "test_id", nullable = false)
    val test: Test? = null,

    @Column(name = "possible_points", nullable = false)
    val possiblePoints: Int = 0,

    @Column(name = "won_points", nullable = true)
    val wonPoints: Int? = null,

    @Column(name = "passed", nullable = false, columnDefinition = "ENUM('YES','NO')")
    @Enumerated(EnumType.STRING)
    val passed: YesNo = YesNo.NO,

    @Column(name = "is_completed", nullable = false, columnDefinition = "ENUM('YES','NO')")
    @Enumerated(EnumType.STRING)
    val isCompleted: YesNo = YesNo.NO
)
