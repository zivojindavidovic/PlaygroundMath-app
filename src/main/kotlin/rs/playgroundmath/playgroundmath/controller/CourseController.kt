package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.response.CreateCourseResponse
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

    private fun Course.toResponse(): CreateCourseResponse {
        return CreateCourseResponse(
            courseId = this.courseId,
            age = this.age,
            dueDate = this.dueDate!!
        )
    }
}