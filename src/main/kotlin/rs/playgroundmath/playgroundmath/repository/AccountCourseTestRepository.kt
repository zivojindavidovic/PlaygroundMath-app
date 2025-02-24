package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.AccountCourseTest
import rs.playgroundmath.playgroundmath.model.AccountCourseTestId

@Repository
interface AccountCourseTestRepository: JpaRepository<AccountCourseTest, AccountCourseTestId> {
    fun findByAccount_AccountIdAndTest_TestId(accountId: Long, testId: Long): AccountCourseTest

    fun findByAccount_AccountIdAndTest_Course_CourseIdAndIsCompleted(accountId: Long, courseId: Long, isCompleted: YesNo): List<AccountCourseTest>
}