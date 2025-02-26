package rs.playgroundmath.playgroundmath.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
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
): UserService {
    override fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse {
        val foundUser = userRepository.findByEmail(userRegisterRequest.email)

        if (foundUser != null) {
            throw UserAlreadyExistsException("Korisnik sa e-adresom je već registrovan")
        }

        var user = User(email = userRegisterRequest.email, password = encoder().encode(userRegisterRequest.password))

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

    private fun User.toTeacherResponse(): UserTeachersResponse =
        UserTeachersResponse(
            teacherId = this.userId,
            teacherEmail = this.email,
            numberOfActiveCourses = 0
        )

    private fun User.toResponse(): UserRegisterResponse {
        return UserRegisterResponse(
            id = this.userId,
            email = this.email
        )
    }

    private fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    override fun getUserAccountCourses(userId: Long): List<UserAccountCoursesResponse> {
        val accountCourses = accountCourseRepository.findAllByAccount_User_UserId(userId)

        return accountCourses.map { it ->
            it.toUserAccountCoursesResponse()
        }
    }

    private fun AccountCourse.toUserAccountCoursesResponse(): UserAccountCoursesResponse {
        return UserAccountCoursesResponse(
            accounts = this.toAccountCoursesResponse()
        )
    }

    private fun AccountCourse.toAccountCoursesResponse(): AccountCoursesResponse {
        val accountId = this.account!!.accountId

        return AccountCoursesResponse(
            accountId = accountId,
            courses = this.course!!.toCoursesResponse(accountId),
        )
    }

    private fun Course.toCoursesResponse(accountId: Long): CoursesResponse {
        val testsCountByCourseId = testRepository.countTestByCourse_CourseId(this.courseId)
        val solvedTestsCountByAccountId = accountCourseTestRepository.countAllByAccount_AccountIdAndIsCompleted(accountId, YesNo.YES)

        return CoursesResponse(
            courseId = this.courseId,
            courseTestsCount = testsCountByCourseId,
            accountSolvedTestsCount = solvedTestsCountByAccountId
        )
    }
}
