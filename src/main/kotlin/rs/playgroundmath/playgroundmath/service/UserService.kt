package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.UserRegisterResponse

interface UserService {

    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse

    fun findByUserId(userId: Long): User?
}