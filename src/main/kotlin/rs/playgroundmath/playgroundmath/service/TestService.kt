package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.payload.response.CourseAccountTestsResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTestsResponse

interface TestService {

    fun saveTest(test: Test): Test

    fun countUnresolvedTestsByAccountId(accountId: Long): Long

    fun findUnresolvedTestsByAccountId(accountId: Long): Test

    fun getTestsRelatedToCourse(courseId: Long): List<CourseTestsResponse>

    fun getUnresolvedTestsRelatedToCourseAndAccount(courseId: Long, accountId: Long): List<CourseAccountTestsResponse>
}
