package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.Course
import java.time.LocalDateTime

@Repository
interface CourseRepository: JpaRepository<Course, Long> {

    fun findByCourseId(courseId: Long): Course

    fun findAllByUser_UserId(userId: Long): List<Course>

    fun findAllByUser_UserIdAndDueDateAfter(userId: Long, dueDate: LocalDateTime): List<Course>

    fun countAllByUser_UserIdAndDueDateAfter(userId: Long, dueDate: LocalDateTime): Long
}