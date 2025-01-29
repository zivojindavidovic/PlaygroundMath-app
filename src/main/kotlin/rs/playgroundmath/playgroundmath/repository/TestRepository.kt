package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.Test

@Repository
interface TestRepository: JpaRepository<Test, Long> {
    fun deleteByAccount_AccountId(accountId: Long)
}