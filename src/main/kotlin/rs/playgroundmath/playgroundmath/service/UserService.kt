package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.UserDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.AdminUserResponse
import rs.playgroundmath.playgroundmath.payload.response.UserRegisterResponse
import rs.playgroundmath.playgroundmath.payload.response.UserTeachersResponse

interface UserService {

    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse

    fun confirmUserRegistration(token: String): Boolean

    fun deleteUser(userDeleteRequest: UserDeleteRequest)

    fun getAllTeachers(): List<UserTeachersResponse>

    fun findByEmail(email: String): User?

    fun findByUserId(userId: Long): User?

    fun getAll(): List<AdminUserResponse>
}
