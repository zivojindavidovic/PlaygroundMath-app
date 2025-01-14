package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.UserRegisterResponse
import rs.playgroundmath.playgroundmath.service.UserService

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/create")
    fun create(@RequestBody userRegisterRequest: UserRegisterRequest): UserRegisterResponse {
        val user = userService.createUser(userRegisterRequest)
        return user.toResponse()
    }

    private fun User.toResponse(): UserRegisterResponse {
        return UserRegisterResponse(
            id = this.userId,
            email = this.email,
            createdAt = this.createdAt
        )
    }
}