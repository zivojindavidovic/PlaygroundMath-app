package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import rs.playgroundmath.playgroundmath.model.AccountCourseTest
import rs.playgroundmath.playgroundmath.model.AccountCourseTestId

interface AccountCourseTestRepository: JpaRepository<AccountCourseTest, AccountCourseTestId> {
    fun findByAccount_AccountIdAndTest_TestId(accountId: Long, testId: Long): AccountCourseTest
}