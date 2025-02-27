package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.AccountCourseTest
import rs.playgroundmath.playgroundmath.model.AccountCourseTestId
import java.time.LocalDateTime

@Repository
interface AccountCourseTestRepository: JpaRepository<AccountCourseTest, AccountCourseTestId> {
    fun findByAccount_AccountIdAndTest_TestId(accountId: Long, testId: Long): AccountCourseTest

    fun findByAccount_AccountIdAndTest_Course_CourseIdAndIsCompletedAndTest_Course_DueDateAfter(accountId: Long, courseId: Long, isCompleted: YesNo, dueDate: LocalDateTime): List<AccountCourseTest>

    fun countAllByAccount_AccountIdAndIsCompleted(accountId: Long, isCompleted: YesNo): Long

    fun countAllByAccount_AccountIdAndTest_Course_CourseIdAndIsCompleted(accountId: Long, courseId: Long, isCompleted: YesNo): Long
}