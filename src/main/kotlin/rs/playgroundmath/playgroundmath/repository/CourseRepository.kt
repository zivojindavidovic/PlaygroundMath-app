package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.Course

@Repository
interface CourseRepository: JpaRepository<Course, Long> {

    fun findByCourseId(courseId: Long): Course
    fun findAllByUser_UserId(userId: Long): List<Course>
}