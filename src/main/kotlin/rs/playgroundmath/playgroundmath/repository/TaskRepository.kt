package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.enums.TaskStatus

@Repository
interface TaskRepository: JpaRepository<Task, Long> {

//    @Query("SELECT SUM(t.points) FROM Task t WHERE t.isCompleted = :status AND t.account.accountId = :accountId")
//    fun sumPointsByAccountAndStatus(@Param("accountId") accountId: Long, @Param("status") status: TaskStatus): Long?
//
//    @Query("SELECT t FROM Task t WHERE t.account.accountId = :accountId and t.isCompleted = :status ORDER BY t.taskId desc LIMIT 30")
//    fun findTop30ByAccountAndStatusOrderByTaskIdDesc(@Param("accountId") accountId: Long, @Param("status") status: TaskStatus): List<Task>
}