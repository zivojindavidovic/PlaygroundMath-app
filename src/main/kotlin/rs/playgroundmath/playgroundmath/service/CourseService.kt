package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.payload.request.ApplyForCourseRequest
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.ResolveApplicationRequest
import rs.playgroundmath.playgroundmath.payload.response.AccountAcceptedCourse
import rs.playgroundmath.playgroundmath.payload.response.CourseApplicationResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseResponse

interface CourseService {

    fun createCourse(courseCreateRequest: CourseCreateRequest): CourseResponse

    fun getMyCourses(): List<CourseResponse>

    fun getCourseById(courseId: Long): CourseResponse

    fun findByCourseId(courseId: Long): Course

    fun findCoursesRelatedToUserId(userId: Long): List<CourseResponse>

    fun applyForCourse(applyForCourseRequest: ApplyForCourseRequest)

    fun getCoursesApplications(userId: Long): List<CourseApplicationResponse>

    fun resolveApplication(resolveApplicationRequest: ResolveApplicationRequest)

    fun getCoursesRelatedToAccount(accountId: Long): List<AccountAcceptedCourse>
}
