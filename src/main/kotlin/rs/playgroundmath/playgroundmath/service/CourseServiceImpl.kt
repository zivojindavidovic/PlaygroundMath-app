package rs.playgroundmath.playgroundmath.service

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.common.Functions
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus
import rs.playgroundmath.playgroundmath.model.*
import rs.playgroundmath.playgroundmath.payload.request.ApplyForCourseRequest
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.ResolveApplicationRequest
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.repository.*

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository,
    private val testRepository: TestRepository,
    private val accountCourseRepository: AccountCourseRepository,
    private val accountCourseTestRepository: AccountCourseTestRepository,
    private val taskRepository: TaskRepository,
    @Lazy private val userService: UserService,
    @Lazy private val accountService: AccountService
): CourseService {

    override fun createCourse(courseCreateRequest: CourseCreateRequest): CourseResponse {
        val user = userService.findByUserId(courseCreateRequest.userId)

        return courseRepository.save(
            Course(
                dueDate = courseCreateRequest.dueDate,
                age = courseCreateRequest.age,
                user = user
            )
        ).toResponse()
    }

    override fun getMyCourses(): List<CourseResponse> =
        courseRepository.findAllByUser_UserId(Functions.getCurrentLoggedInUserId()!!).map {
            it.toResponse()
        }

    override fun getCourseById(courseId: Long): CourseResponse =
        courseRepository.findById(courseId).get().toResponse()

    override fun findCoursesRelatedToUserId(userId: Long): List<CourseResponse> {
        return courseRepository.findAllByUser_UserId(userId).map {
            it.toResponse()
        }
    }

    override fun applyForCourse(applyForCourseRequest: ApplyForCourseRequest) {
        val course = courseRepository.findByCourseId(applyForCourseRequest.courseId)
        val account = accountService.findByAccountId(applyForCourseRequest.accountId)

        accountCourseRepository.save(AccountCourse(account = account, course = course, status = AccountCourseStatus.PENDING))
    }

    override fun getCoursesApplications(userId: Long): List<CourseApplicationResponse> {
        val applications = accountCourseRepository.findAllByCourse_User_UserIdAndStatus(userId, AccountCourseStatus.PENDING)

        return applications.map {
            it.toCourseApplicationResponse()
        }
    }

    override fun resolveApplication(resolveApplicationRequest: ResolveApplicationRequest) {
        val foundAccountCourse = accountCourseRepository.findByCourse_CourseIdAndAccount_AccountId(resolveApplicationRequest.courseId, resolveApplicationRequest.accountId)
            if (resolveApplicationRequest.decision) {
            val updatedAccountCourse = foundAccountCourse.copy(
                status = AccountCourseStatus.ACCEPTED
            )

            val account = accountService.findByAccountId(resolveApplicationRequest.accountId)
            val tests = testRepository.findAllByCourse_CourseId(courseId = resolveApplicationRequest.courseId)

            tests.forEach { test ->
                val currentTestId = test.testId
                val possiblePoints = taskRepository.sumPointsByTestId(currentTestId)

                accountCourseTestRepository.save(AccountCourseTest(account = account, test = test, possiblePoints = possiblePoints.toInt()))
            }

            accountCourseRepository.save(updatedAccountCourse)
        } else {
            val updatedAccountCourse = foundAccountCourse.copy(
                status = AccountCourseStatus.DECLINED
            )

            accountCourseRepository.save(updatedAccountCourse)
        }
    }

    override fun getCoursesRelatedToAccount(accountId: Long): List<AccountAcceptedCourse> {
        val foundAccountCourses = accountCourseRepository.findAllByAccount_AccountIdAndStatus(accountId, AccountCourseStatus.ACCEPTED)

        return foundAccountCourses.map {
            it.toAccountAcceptedCourse()
        }
    }

    override fun findByCourseId(courseId: Long): Course =
        courseRepository.findByCourseId(courseId)

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

    private fun Course.toResponse(): CourseResponse {
        return CourseResponse(
            courseId = this.courseId,
            age = this.age,
            dueDate = this.dueDate!!
        )
    }
}
