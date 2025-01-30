package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.response.CourseResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTestsResponse
import rs.playgroundmath.playgroundmath.payload.response.CreateCourseResponse
import rs.playgroundmath.playgroundmath.repository.CourseRepository
import rs.playgroundmath.playgroundmath.service.CourseService

@RestController
@RequestMapping("/api/v1/course")
class CourseController(
    private val courseService: CourseService
) {

    @PostMapping("/create")
    fun create(@RequestBody courseCreateRequest: CourseCreateRequest): CreateCourseResponse {
        return courseService.createCourse(courseCreateRequest).toResponse()
    }

    @GetMapping("/myCourses")
    fun getMyCourses(): List<CreateCourseResponse> =
        courseService.getMyCourses().map {
            it.toResponse()
        }

    @GetMapping("/{courseId}")
    fun getCourse(@PathVariable courseId: Long): CourseResponse =
        courseService.getCourse(courseId).toCourseResponse()

    private fun Course.toCourseResponse(): CourseResponse =
        CourseResponse(
            courseId = this.courseId,
            dueDate = this.dueDate,
            age = this.age
        )

    @GetMapping("/{courseId}/tests")
    fun getCourseTests(@PathVariable courseId: Long): List<CourseTestsResponse> =
        courseService.getCourseTests(courseId = courseId)

    private fun Course.toResponse(): CreateCourseResponse {
        return CreateCourseResponse(
            courseId = this.courseId,
            age = this.age,
            dueDate = this.dueDate!!
        )
    }
}