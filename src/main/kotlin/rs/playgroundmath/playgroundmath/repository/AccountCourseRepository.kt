package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus
import rs.playgroundmath.playgroundmath.model.AccountCourse
import rs.playgroundmath.playgroundmath.model.AccountCourseId

interface AccountCourseRepository: JpaRepository<AccountCourse, AccountCourseId> {
    fun deleteById_AccountId(accountId: Long)

    fun findAllByCourse_User_UserIdAndStatus(userId: Long, status: AccountCourseStatus): List<AccountCourse>

    fun findByCourse_CourseIdAndAccount_AccountId(courseId: Long, accountId: Long): AccountCourse
}