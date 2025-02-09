package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.payload.response.CourseAccountTestsResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTestsResponse
import rs.playgroundmath.playgroundmath.service.TestService

@RestController
@RequestMapping("/api/v1/test")
class TestController(
    private val testService: TestService
) {

    @GetMapping("/all")
    fun getTestsRelatedToCourse(@RequestParam courseId: Long): List<CourseTestsResponse> =
        testService.getTestsRelatedToCourse(courseId)

    @GetMapping("/unresolved")
    fun getUnresolvedTestsRelatedToCourseAndAccount(@RequestParam courseId: Long, @RequestParam accountId: Long): List<CourseAccountTestsResponse> =
        testService.getUnresolvedTestsRelatedToCourseAndAccount(courseId, accountId)
}
