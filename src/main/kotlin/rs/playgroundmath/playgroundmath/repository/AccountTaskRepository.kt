package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.AccountTask
import rs.playgroundmath.playgroundmath.model.AccountTaskId

@Repository
interface AccountTaskRepository: JpaRepository<AccountTask, AccountTaskId> {
}