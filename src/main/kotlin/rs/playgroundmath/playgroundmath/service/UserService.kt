package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.AdminUpdateUserRequest
import rs.playgroundmath.playgroundmath.payload.request.UserDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.*

interface UserService {

    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse

    fun confirmUserRegistration(token: String): Boolean

    fun deleteUser(userDeleteRequest: UserDeleteRequest)

    fun getAllTeachers(): List<UserTeachersResponse>

    fun findByEmail(email: String): User?

    fun findByUserId(userId: Long): User?

    fun getAll(): List<AdminUserResponse>

    fun deleteUserByAdmin(userId: Long)

    fun getUserAccountCourses(userId: Long): UserAccountCoursesResponse

    fun getUserAccountTests(accountId: Long, courseId: Long): AccountTestsResponse

    fun getTeacherCourseInformation(teacherId: Long, courseId: Long): TeacherCourseInformationResponse

    fun getById(userId: Long): UserResponse

    fun updateUserByAdmin(adminUpdateUserRequest: AdminUpdateUserRequest)
}
