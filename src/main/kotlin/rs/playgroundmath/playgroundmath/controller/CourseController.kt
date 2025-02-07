package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.payload.request.ApplyForCourseRequest
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.ResolveApplicationRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.response.*
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

    @PostMapping("/apply")
    fun apply(@RequestBody applyForCourseRequest: ApplyForCourseRequest): ApplicationForCourseResponse =
        courseService.applyForCourse(applyForCourseRequest)

    @GetMapping("/{userId}/applications")
    fun getCoursesApplications(@PathVariable userId: Long): List<CourseApplicationResponse> {
        return courseService.getCoursesApplications(userId)
    }

    @PostMapping("/resolveApplication")
    fun resolveApplication(@RequestBody resolveApplicationRequest: ResolveApplicationRequest): ResolveApplicationResponse =
        courseService.resolveApplication(resolveApplicationRequest)

    @GetMapping("/{accountId}/list")
    fun getCoursesRelatedToAccount(@PathVariable accountId: Long):List<AccountAcceptedCourse> {
        return courseService.getCoursesRelatedToAccount(accountId)
    }

    @GetMapping("{accountId}/unresolvedTests")
    fun getUnresolvedTests(@PathVariable accountId: Long): List<CourseAccountTestsResponse> =
        courseService.getUnresolvedTests(accountId)

    @PostMapping("/solveTest")
    fun solveTest(@RequestBody solveTestRequest: SolveTestRequest): Any? {
        return courseService.solveTest(solveTestRequest)
    }

    private fun Course.toResponse(): CreateCourseResponse {
        return CreateCourseResponse(
            courseId = this.courseId,
            age = this.age,
            dueDate = this.dueDate!!
        )
    }
}