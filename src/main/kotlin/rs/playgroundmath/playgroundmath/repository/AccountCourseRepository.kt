package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import rs.playgroundmath.playgroundmath.model.AccountCourse
import rs.playgroundmath.playgroundmath.model.AccountCourseId

interface AccountCourseRepository: JpaRepository<AccountCourse, AccountCourseId> {
    fun deleteById_AccountId(accountId: Long)
}