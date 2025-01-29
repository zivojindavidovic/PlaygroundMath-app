package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.common.Functions
import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.repository.CourseRepository
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) {

    fun createCourse(courseCreateRequest: CourseCreateRequest): Course {
        val foundUser = userRepository.findById(courseCreateRequest.userId)
        return courseRepository.save(Course(dueDate = courseCreateRequest.dueDate, age = courseCreateRequest.age, user = foundUser.get()))
    }

    fun getMyCourses(): List<Course> =
        courseRepository.findAllByUser_UserId(Functions.getCurrentLoggedInUserId()!!)

    fun findAllByUserId(userId: Long): List<Course> =
        courseRepository.findAllByUser_UserId(userId)
}