package rs.playgroundmath.playgroundmath.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.exceptions.UserAlreadyExistsException
import rs.playgroundmath.playgroundmath.exceptions.UserNotFoundException
import rs.playgroundmath.playgroundmath.model.Role
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.model.Course
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.DeleteUserResponse
import rs.playgroundmath.playgroundmath.payload.response.UserAccountsResponse
import rs.playgroundmath.playgroundmath.payload.response.UserTeachersResponse
import rs.playgroundmath.playgroundmath.repository.UserRepository
import rs.playgroundmath.playgroundmath.payload.response.UserTeacherCourseResponse

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val courseService: CourseService
) {
    fun createUser(userRegisterRequest: UserRegisterRequest): User {
        val foundUser = userRepository.findByEmail(userRegisterRequest.email)

        if (foundUser != null) {
            throw UserAlreadyExistsException("User with email ${userRegisterRequest.email} already exists")
        }

        var user = User(email = userRegisterRequest.email, password = encoder().encode(userRegisterRequest.password))

        if (userRegisterRequest.accountType == RoleType.TEACHER) {
           user = user.copy(role = Role(roleId = 3, RoleType.TEACHER))
        }

        return userRepository.save(user)
    }

    fun deleteUser(userId: Long): DeleteUserResponse {
        val foundUser = userRepository.findById(userId)

        if (foundUser.isPresent) {
            userRepository.delete(foundUser.get())
        } else {
            throw UserNotFoundException("User with ID: $userId not found")
        }

        return DeleteUserResponse("User successfully deleted")
    }

    fun getUserByEmail(email: String): User? =
        userRepository.findByEmail(email)

    fun getAllTeachers(): List<UserTeachersResponse> {
        val role = roleService.findRoleByRoleType(RoleType.TEACHER)

        val teachers = userRepository.findAllByRole_RoleId(roleId = role!!.roleId)
        return teachers.map {
            it.toTeacherResponse()
        }
    }

    fun getAllTeacherCourses(teacherId: Long): List<UserTeacherCourseResponse> {
        val courses = courseService.findAllByUserId(teacherId)
        return courses.map {
            it.toUserTeacherCourseResponse()
        }
    }

    fun getAllChildren(userId: Long): List<UserAccountsResponse> {
        return userRepository.findByUserId(userId).accounts.map {
            it.toUserAccountsResponse()
        }
    }

    private fun Account.toUserAccountsResponse(): UserAccountsResponse {
        return UserAccountsResponse(
            accountId = this.accountId,
            accountData = this.username + " " + this.age + "y"
        )
    }

    private fun Course.toUserTeacherCourseResponse(): UserTeacherCourseResponse =
        UserTeacherCourseResponse(
            courseId = this.courseId,
            age = this.age.toLong(),
            dueDate = this.dueDate.toString()
        )

    private fun User.toTeacherResponse(): UserTeachersResponse =
        UserTeachersResponse(
            teacherId = this.userId,
            teacherEmail = this.email,
            numberOfActiveCourses = 0
        )

    private fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
}