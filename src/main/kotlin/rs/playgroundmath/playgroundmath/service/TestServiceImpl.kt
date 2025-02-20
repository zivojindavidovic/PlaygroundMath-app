package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.AccountCourse
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.payload.response.CourseAccountTestsResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTaskResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTestResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTestsResponse
import rs.playgroundmath.playgroundmath.repository.AccountCourseRepository
import rs.playgroundmath.playgroundmath.repository.TestRepository

@Service
class TestServiceImpl(
    private val testRepository: TestRepository,
    private val accountCourseRepository: AccountCourseRepository
): TestService {

    override fun saveTest(test: Test): Test =
        testRepository.save(test)

    override fun countUnresolvedTestsByAccountId(accountId: Long): Long =
        testRepository.countByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)

    override fun findUnresolvedTestsByAccountId(accountId: Long): Test =
        testRepository.findByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)

    override fun getTestsRelatedToCourse(courseId: Long): List<CourseTestsResponse> =
        testRepository.findAllByCourse_CourseId(courseId).map {
            it.toCourseTestsResponse()
        }

    override fun getUnresolvedTestsRelatedToCourseAndAccount(
        courseId: Long,
        accountId: Long
    ): List<CourseAccountTestsResponse> {
        val foundAccountCourses = accountCourseRepository.findAllByAccount_AccountIdAndStatusAndCourse_CourseId(accountId, AccountCourseStatus.ACCEPTED, courseId)

        return foundAccountCourses.map {
            it.toCourseAccountTestsResponse()
        }
    }

    private fun Test.toCourseTestsResponse(): CourseTestsResponse =
        CourseTestsResponse(
            testId = this.testId,
            courseId = this.course?.courseId!!
        )

    private fun AccountCourse.toCourseAccountTestsResponse(): CourseAccountTestsResponse =
        CourseAccountTestsResponse(
            courseId = this.course!!.courseId,
            tests = this.course.tests.map {
                it.toCourseTestResponse()
            }
        )

    private fun Test.toCourseTestResponse(): CourseTestResponse =
        CourseTestResponse(
            testId = this.testId,
            tasks = this.tasks!!.map {
                it.toCourseTaskResponse()
            }
        )

    private fun Task.toCourseTaskResponse(): CourseTaskResponse =
        CourseTaskResponse(
            taskId = this.taskId,
            task = this.firstNumber + " " + this.operation + " " + this.secondNumber
        )
}
