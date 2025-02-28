package rs.playgroundmath.playgroundmath.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.payload.request.ApplyForCourseRequest
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.ResolveApplicationRequest
import rs.playgroundmath.playgroundmath.payload.request.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.service.CourseService

@RestController
@RequestMapping("/api/v1/course")
class CourseController(
    private val courseService: CourseService
) {

    @PostMapping("/create")
    fun createCourse(@Valid @RequestBody courseCreateRequest: CourseCreateRequest): ResponseEntity<ApiResponse<CourseResponse>> {
        val results = courseService.createCourse(courseCreateRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(results)
            )
        )
    }

    @GetMapping("/myCourses")
    fun getMyCourses(): ResponseEntity<ApiResponse<CourseResponse>> {
        val results = courseService.getMyCourses()

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = results
            )
        )
    }

    @GetMapping
    fun getCourse(@RequestParam courseId: Long): ResponseEntity<ApiResponse<CourseResponse>> {
        val results = courseService.getCourseById(courseId)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(results)
            )
        )
    }

    @GetMapping("/all")
    fun getAllCoursesRelatedToProfessor(@RequestParam professorId: Long): List<CourseResponse> =
        courseService.findCoursesRelatedToUserId(professorId)

    @PostMapping("/apply")
    fun apply(@RequestBody applyForCourseRequest: ApplyForCourseRequest): ResponseEntity<ApiResponse<Any>> {
        courseService.applyForCourse(applyForCourseRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = emptyList()
            )
        )
    }

    @GetMapping("/applications")
    fun getCoursesApplications(@RequestParam userId: Long): List<CourseApplicationResponse> =
         courseService.getCoursesApplications(userId)

    @PostMapping("/resolveApplication")
    fun resolveApplication(@RequestBody resolveApplicationRequest: ResolveApplicationRequest): ResponseEntity<ApiResponse<Any>> {
        courseService.resolveApplication(resolveApplicationRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = emptyList()
            )
        )
    }

    @GetMapping("/list")
    fun getCoursesRelatedToAccount(@RequestParam accountId: Long): List<AccountAcceptedCourse> =
        courseService.getCoursesRelatedToAccount(accountId)
}
