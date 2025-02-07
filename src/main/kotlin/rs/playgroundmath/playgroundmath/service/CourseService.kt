package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.common.Functions
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus
import rs.playgroundmath.playgroundmath.model.AccountCourse
import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.model.Task
import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.payload.request.ApplyForCourseRequest
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.ResolveApplicationRequest
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.repository.*

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
    private val testRepository: TestRepository,
    private val accountRepository: AccountRepository,
    private val accountCourseRepository: AccountCourseRepository
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

    fun applyForCourse(applyForCourseRequest: ApplyForCourseRequest): ApplicationForCourseResponse {
        val foundCourse = courseRepository.findByCourseId(applyForCourseRequest.courseId)
        val foundAccount = accountRepository.findByAccountId(applyForCourseRequest.accountId)

        accountCourseRepository.save(AccountCourse(account = foundAccount, course = foundCourse, status = AccountCourseStatus.PENDING))

        return ApplicationForCourseResponse(message = "Application sent successfully")
    }

    fun getCoursesApplications(userId: Long): List<CourseApplicationResponse> {
        val foundApplications = accountCourseRepository.findAllByCourse_User_UserIdAndStatus(userId, AccountCourseStatus.PENDING)

        return foundApplications.map {
            it.toCourseApplicationResponse()
        }
    }

    fun resolveApplication(resolveApplicationRequest: ResolveApplicationRequest): ResolveApplicationResponse {
        val foundAccountCourse = accountCourseRepository.findByCourse_CourseIdAndAccount_AccountId(resolveApplicationRequest.courseId, resolveApplicationRequest.accountId)

        return if (resolveApplicationRequest.decision) {
            val updatedAccountCourse = foundAccountCourse.copy(
                status = AccountCourseStatus.ACCEPTED
            )

            accountCourseRepository.save(updatedAccountCourse)
            ResolveApplicationResponse(message = "Application accepted")
        } else {
            val updatedAccountCourse = foundAccountCourse.copy(
                status = AccountCourseStatus.DECLINED
            )

            accountCourseRepository.save(updatedAccountCourse)
            ResolveApplicationResponse(message = "Application declined")
        }
    }

    fun getCoursesRelatedToAccount(accountId: Long): List<AccountAcceptedCourse> {
        val foundAccountCourses = accountCourseRepository.findAllByAccount_AccountIdAndStatus(accountId, AccountCourseStatus.ACCEPTED)

        return foundAccountCourses.map {
            it.toAccountAcceptedCourse()
        }
    }

    fun getUnresolvedTests(accountId: Long): List<CourseAccountTestsResponse> {
        val foundAccountCourses = accountCourseRepository.findAllByAccount_AccountIdAndStatus(accountId, AccountCourseStatus.ACCEPTED)

        val tmp = foundAccountCourses.map {
            it.toCourseAccountTestsResponse()
        }

        return tmp
    }

    private fun AccountCourse.toCourseAccountTestsResponse(): CourseAccountTestsResponse =
        CourseAccountTestsResponse(
            courseId = this.course!!.courseId,
            tests = this.course.tests.map {
                it.tasks!!.map {
                    it.toCourseTaskResponse()
                }
            }
        )

    private fun Task.toCourseTaskResponse(): CourseTaskResponse =
        CourseTaskResponse(
            taskId = this.taskId,
            task = this.firstNumber + " " + this.operation + " " + this.secondNumber
        )

    private fun AccountCourse.toAccountAcceptedCourse(): AccountAcceptedCourse =
        AccountAcceptedCourse(
            courseId = this.course!!.courseId,
            courseAge = this.course.age,
        )

    private fun AccountCourse.toCourseApplicationResponse(): CourseApplicationResponse =
        CourseApplicationResponse(
            courseId = this.course!!.courseId,
            courseAge = this.course.age,
            accountAge = this.account!!.age,
            accountId = this.account.accountId,
            accountUsername = this.account.username
        )

    private fun Test.toCourseTestsResponse(): CourseTestsResponse =
        CourseTestsResponse(
            testId = this.testId,
            courseId = this.course?.courseId!!
        )
}