package rs.playgroundmath.playgroundmath.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.AccountCourseStatus
import rs.playgroundmath.playgroundmath.exceptions.UserAlreadyExistsException
import rs.playgroundmath.playgroundmath.exceptions.UserNotFoundException
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.enums.Status
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.exceptions.DeleteUserPasswordDoNotMatchException
import rs.playgroundmath.playgroundmath.model.*
import rs.playgroundmath.playgroundmath.payload.request.UserDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.repository.*
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val communicationService: CommunicationService,
    private val confirmationTokenService: ConfirmationTokenService,
    private val accountCourseRepository: AccountCourseRepository,
    private val testRepository: TestRepository,
    private val accountCourseTestRepository: AccountCourseTestRepository,
    private val accountTaskRepository: AccountTaskRepository,
    private val accountRepository: AccountRepository,
    private val courseRepository: CourseRepository,
): UserService {
    override fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse {
        val foundUser = userRepository.findByEmail(userRegisterRequest.email)

        if (foundUser != null) {
            throw UserAlreadyExistsException("Korisnik sa e-adresom je već registrovan")
        }

        var user = User(email = userRegisterRequest.email, password = encoder().encode(userRegisterRequest.password), firstName = userRegisterRequest.firstName, lastName = userRegisterRequest.lastName)

        if (userRegisterRequest.accountType == RoleType.TEACHER) {
           user = user.copy(role = Role(roleId = 3, RoleType.TEACHER))
        }

        val savedUser = userRepository.save(user)

        val confirmationToken = confirmationTokenService.createConfirmationToken(savedUser)
        val confirmationLink = "http://0.0.0.0:5173/confirm?token=${confirmationToken.token}"

        communicationService.sendUserRegistrationConfirmationEmail(user.email, confirmationLink)

        return savedUser.toResponse()
    }

    override fun confirmUserRegistration(token: String): Boolean {
        val confirmationToken = confirmationTokenService.findTokenByToken(token) ?: return false

        val isTokenExpired = confirmationToken.expiresAt.isBefore(LocalDateTime.now())
        val user = findByUserId(confirmationToken.userId)

        return if (!isTokenExpired) {
            val updatedUser = user!!.copy(
                status = Status.ACTIVE
            )

            userRepository.save(updatedUser)

            true
        } else {
            communicationService.sendUserRegistrationConfirmationEmail(user!!.email, "http://0.0.0.0:5173/confirm?token=${token}")
            false
        }
    }

    override fun deleteUser(userDeleteRequest: UserDeleteRequest) {
        val user = findByUserId(userDeleteRequest.userId) ?: throw UserNotFoundException("User with ID: ${userDeleteRequest.userId} not found")

        val isPasswordValid = encoder().matches(userDeleteRequest.password, user.password)

        if (!isPasswordValid) {
            throw DeleteUserPasswordDoNotMatchException("Uneta šifra nije ispravna. Nalog nije obrisan")
        }

        userRepository.delete(user)
    }

    override fun getAllTeachers(): List<UserTeachersResponse> {
        val role = roleService.findRoleByRoleType(RoleType.TEACHER)

        val teachers = userRepository.findAllByRole_RoleId(roleId = role!!.roleId)
        return teachers.map {
            it.toTeacherResponse()
        }
    }

    override fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)


    override fun findByUserId(userId: Long): User? =
        userRepository.findByUserId(userId)

    override fun getAll(): List<AdminUserResponse> {
        val users = userRepository.findAllByRole_RoleTypeNot(RoleType.ADMIN)

        return users.map { user ->
            AdminUserResponse(
                id = user.userId,
                email = user.email,
                isParent = user.role.roleType == RoleType.PARENT,
                isTeacher = user.role.roleType == RoleType.TEACHER
            )
        }
    }

    override fun deleteUserByAdmin(userId: Long) {
        userRepository.deleteById(userId)
    }

    private fun User.toTeacherResponse(): UserTeachersResponse {
        val activeCourses = courseRepository.countAllByUser_UserIdAndDueDateAfter(this.userId, LocalDateTime.now())

        return UserTeachersResponse(
                teacherId = this.userId,
                teacherEmail = this.email,
                numberOfActiveCourses = activeCourses.toInt()
            )
    }


    private fun User.toResponse(): UserRegisterResponse {
        return UserRegisterResponse(
            id = this.userId,
            email = this.email
        )
    }

    private fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    override fun getUserAccountCourses(userId: Long): UserAccountCoursesResponse {
        val accountCourses = accountCourseRepository.findAllByAccount_User_UserIdAndAndStatus(userId, AccountCourseStatus.ACCEPTED)

        val groupedByAccount = accountCourses.groupBy { it.account!!.accountId }

        val accountsResponse = groupedByAccount.map { (accountId, accountCoursesList) ->
            val courses = accountCoursesList.map { accountCourse ->
                val course = accountCourse.course!!
                val testsCountByCourseId = testRepository.countTestByCourse_CourseId(course.courseId)
                val solvedTestsCountByAccountId =
                    accountCourseTestRepository.countAllByAccount_AccountIdAndTest_Course_CourseIdAndIsCompleted(accountId, course.courseId, YesNo.YES)

                CoursesResponse(
                    courseId = course.courseId,
                    courseTestsCount = testsCountByCourseId,
                    accountSolvedTestsCount = solvedTestsCountByAccountId
                )
            }

            AccountCoursesResponse(
                accountId = accountId,
                courses = courses
            )
        }

        return UserAccountCoursesResponse(accounts = accountsResponse)
    }

    override fun getUserAccountTests(accountId: Long, courseId: Long): AccountTestsResponse {
        val accountTasks = accountTaskRepository.findAllByAccount_AccountIdAndTask_Test_Course_CourseId(accountId, courseId)

        val groupedByTest = accountTasks.groupBy { it.task!!.test!!.testId }

        val testsResponse = groupedByTest.map { (testId, accountTasksList) ->
            val tasks = accountTasksList.map { task ->
                val answer = task.answer ?: ""

                TaskResponse(
                    taskId = task.task!!.taskId,
                    task = task.task.firstNumber + " " + task.task.operation + " " + task.task.secondNumber + " = " + answer
                )
            }

            val accountCourseTest = accountCourseTestRepository.findByAccount_AccountIdAndTest_TestId(accountId, testId)
            val isCompleted = accountCourseTest.isCompleted == YesNo.YES

            TestResponse(
                testId = testId,
                isCompleted = isCompleted,
                tasks = tasks
            )
        }

        return AccountTestsResponse(tests = testsResponse)
    }

    override fun getTeacherCourseInformation(teacherId: Long, courseId: Long): TeacherCourseInformationResponse {
        val accountCourse = accountCourseRepository.findAllByCourse_CourseIdAndStatus(courseId, AccountCourseStatus.ACCEPTED)
        val totalTests = testRepository.countTestByCourse_CourseId(courseId)

        val groupedByAccount = accountCourse.groupBy { it.account!!.accountId }

        val teacherAccountResponse = groupedByAccount.map { (accountId, accountCourseList) ->
            val accountSolvedTests = accountCourseTestRepository.countAllByAccount_AccountIdAndTest_Course_CourseIdAndIsCompleted(accountId, courseId, YesNo.YES)
            val account = accountRepository.findByAccountId(accountId)
            TeacherCourseInformationAccountsResponse(
                accountId = accountId,
                username = account.username,
                solvedTest = accountSolvedTests
            )
        }

        val courseTests = testRepository.findAllByCourse_CourseId(courseId)

        val teacherTestsResponse = courseTests.map { test ->
            TeacherCourseInformationTestsResponse(
                testId = test.testId,
                tasks = test.tasks!!.map { task ->
                    TaskResponse(
                        taskId = task.taskId,
                        task = task.firstNumber + " " + task.operation + " " + task.secondNumber + " = " + task.result
                    )
                }
            )
        }

        return TeacherCourseInformationResponse(
            courseId = courseId,
            totalTests = totalTests,
            accounts = teacherAccountResponse,
            tests = teacherTestsResponse
        )
    }

    override fun getById(userId: Long): UserResponse {
        val user = userRepository.findByUserId(userId) ?: throw UserNotFoundException("User with ID: $userId not found")

        val accounts = user.accounts

        val accountsResponse = accounts.map { account ->
            AccountResponse(
                accountId = account.accountId,
                username = account.username,
                points = account.points
            )
        }

        return UserResponse(
            id = user.userId,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            accounts = accountsResponse
        )
    }
}
