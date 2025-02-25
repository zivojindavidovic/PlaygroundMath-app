package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.AccountCourseTest
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.payload.response.CourseAccountTestsResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTaskResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTestResponse
import rs.playgroundmath.playgroundmath.payload.response.CourseTestsResponse
import rs.playgroundmath.playgroundmath.repository.AccountCourseTestRepository
import rs.playgroundmath.playgroundmath.repository.TestRepository

@Service
class TestServiceImpl(
    private val testRepository: TestRepository,
    private val accountCourseTestRepository: AccountCourseTestRepository
): TestService {

    override fun saveTest(test: Test): Test =
        testRepository.save(test)

    override fun countUnresolvedTestsByAccountId(accountId: Long): Long =
        testRepository.countByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)

    override fun findUnresolvedTestsByAccountId(accountId: Long): Test =
        testRepository.findByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)

    override fun findByTestId(accountId: Long): Test =
        testRepository.findByTestId(accountId)

    override fun getTestsRelatedToCourse(courseId: Long): List<CourseTestsResponse> =
        testRepository.findAllByCourse_CourseId(courseId).map {
            it.toCourseTestsResponse()
        }

    override fun getUnresolvedTestsRelatedToCourseAndAccount(
        courseId: Long,
        accountId: Long
    ): List<CourseAccountTestsResponse> {
        val foundAccountCourses = accountCourseTestRepository.findByAccount_AccountIdAndTest_Course_CourseIdAndIsCompleted(accountId, courseId, YesNo.NO)

        return foundAccountCourses.map {
            it.toCourseAccountTestsResponse()
        }
    }

    private fun AccountCourseTest.toCourseAccountTestsResponse(): CourseAccountTestsResponse =
        CourseAccountTestsResponse(
            courseId = this.test!!.course!!.courseId,
            tests = this.test.course!!.tests.map {
                it.toCourseTestResponse()
            }
        )

    private fun Test.toCourseTestsResponse(): CourseTestsResponse =
        CourseTestsResponse(
            testId = this.testId,
            courseId = this.course?.courseId!!
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
