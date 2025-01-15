package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.model.TaskStatus

@Repository
interface TaskRepository: JpaRepository<Task, Long> {

    @Query("SELECT SUM(t.points) FROM Task t WHERE t.isCompleted = :status AND t.account.accountId = :accountId")
    fun sumPointsByAccountAndStatus(@Param("accountId") accountId: Long, @Param("status") status: TaskStatus): Long?
}