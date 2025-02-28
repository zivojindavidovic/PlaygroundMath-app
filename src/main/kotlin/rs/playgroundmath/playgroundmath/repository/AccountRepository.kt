package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.model.User

@Repository
interface AccountRepository: JpaRepository<Account, Long> {
    fun findByAccountId(accountId: Long): Account

    fun countByUser(user: User): Long

    fun findAllByUser(user: User): List<Account>

    fun findAllByOrderByPointsDesc(): List<Account>

    fun findByUser_UserId(userId: Long): List<Account>

    @Query(
        """
        SELECT a 
        FROM Account a
        WHERE a.user.userId = :userId
          AND NOT EXISTS (
              SELECT 1
              FROM a.courses c
              WHERE c.courseId = :courseId
          )
        """
    )
    fun findAllByUserIdAndNotHavingCourse(
        @Param("userId") userId: Long,
        @Param("courseId") courseId: Long
    ): List<Account>
}