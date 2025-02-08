package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.Course

interface CourseService {

    fun findByCourseId(courseId: Long): Course
}