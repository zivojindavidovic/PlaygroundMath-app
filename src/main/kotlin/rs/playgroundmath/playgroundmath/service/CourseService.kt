package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.common.Functions
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.*
import rs.playgroundmath.playgroundmath.payload.request.ApplyForCourseRequest
import rs.playgroundmath.playgroundmath.payload.request.CourseCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.ResolveApplicationRequest
import rs.playgroundmath.playgroundmath.payload.request.SolveTestRequest
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.repository.*

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
    private val testRepository: TestRepository,
    private val accountRepository: AccountRepository,
    private val accountCourseRepository: AccountCourseRepository,
    private val accountTestRepository: AccountCourseTestRepository,
    private val accountCourseTestRepository: AccountCourseTestRepository,
    private val taskRepository: TaskRepository
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

            val account = accountRepository.findByAccountId(accountId = resolveApplicationRequest.accountId)
            val tests = testRepository.findAllByCourse_CourseId(courseId = resolveApplicationRequest.courseId)

            tests.forEach { test ->
                val currentTestId = test.testId
                val possiblePoints = taskRepository.sumPointsByTestId(currentTestId)

                accountCourseTestRepository.save(AccountCourseTest(account = account, test = test, possiblePoints = possiblePoints.toInt()))
            }


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

    fun solveTest(solveTestRequest: SolveTestRequest): Any? {
        var points = 0
        val unresolvedTest = testRepository.findByTestId(solveTestRequest.testId)
        val tasks = taskRepository.findAllByTest(unresolvedTest)
        tasks.forEach() {
            val currentTaskId = it.taskId
            val userAnswer = solveTestRequest.testAnswers.firstNotNullOfOrNull { it[currentTaskId] }

            if (userAnswer!!.toLong() == it.result.toLong()) {
                points += it.points
            }
        }

        val foundAccountCourseTest = accountCourseTestRepository.findByAccount_AccountIdAndTest_TestId(accountId = solveTestRequest.accountId, testId = unresolvedTest.testId)

        val possiblePoints = foundAccountCourseTest.possiblePoints
        val isPassed = points > (possiblePoints * 70) / 100

        val updatedAccountCourseTest = foundAccountCourseTest.copy(
            wonPoints = points,
            passed = if (isPassed) YesNo.YES else YesNo.NO,
            isCompleted = YesNo.YES
        )

        accountCourseTestRepository.save(updatedAccountCourseTest)

        return if (isPassed) {
            SolveCourseTestResponse(message = "You passed test")
        } else {
            SolveCourseTestResponse(message = "You failed test")
        }
    }

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