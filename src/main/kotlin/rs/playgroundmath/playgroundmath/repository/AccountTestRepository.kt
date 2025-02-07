package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import rs.playgroundmath.playgroundmath.model.AccountTest
import rs.playgroundmath.playgroundmath.model.AccountTestId

interface AccountTestRepository: JpaRepository<AccountTest, AccountTestId> {
}