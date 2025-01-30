package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.model.User

@Repository
interface AccountRepository: JpaRepository<Account, Long> {
    fun findByAccountId(accountId: Long): Account

    fun countByUser(user: User): Long

    fun findAllByUser(user: User): List<Account>

    fun findAllByOrderByPointsDesc(): List<Account>
}