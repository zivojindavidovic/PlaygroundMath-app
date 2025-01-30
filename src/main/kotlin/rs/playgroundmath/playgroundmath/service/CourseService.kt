package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.common.Functions
import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.response.CourseTestsResponse
import rs.playgroundmath.playgroundmath.repository.CourseRepository
import rs.playgroundmath.playgroundmath.repository.TestRepository
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
    private val testRepository: TestRepository
) {

    fun createCourse(courseCreateRequest: CourseCreateRequest): Course {
        val foundUser = userRepository.findById(courseCreateRequest.userId)
        return courseRepository.save(Course(dueDate = courseCreateRequest.dueDate, age = courseCreateRequest.age, user = foundUser.get()))
    }

    fun getMyCourses(): List<Course> =
        courseRepository.findAllByUser_UserId(Functions.getCurrentLoggedInUserId()!!)

    fun findAllByUserId(userId: Long): List<Course> =
        courseRepository.findAllByUser_UserId(userId)

    fun getCourse(courseId: Long): Course =
        courseRepository.findById(courseId).get()

    fun getCourseTests(courseId: Long): List<CourseTestsResponse> {
        return testRepository.findAllByCourse_CourseId(courseId).map {
            it.toCourseTestsResponse()
        }
    }

    private fun Test.toCourseTestsResponse(): CourseTestsResponse =
        CourseTestsResponse(
            testId = this.testId,
            courseId = this.course?.courseId!!
        )
}