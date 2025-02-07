package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.DeleteUserResponse
import rs.playgroundmath.playgroundmath.payload.response.UserAccountsResponse
import rs.playgroundmath.playgroundmath.payload.response.UserRegisterResponse
import rs.playgroundmath.playgroundmath.payload.response.UserTeachersResponse
import rs.playgroundmath.playgroundmath.payload.response.UserTeacherCourseResponse
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

    @DeleteMapping("/delete/{userId}")
    fun delete(@PathVariable userId: Long): DeleteUserResponse =
        userService.deleteUser(userId)

    @GetMapping("/teachers")
    fun getAllTeachers(): List<UserTeachersResponse> {
        return userService.getAllTeachers()
    }

    @GetMapping("/teachers/{teacherId}/courses")
    fun getAllTeacherCourses(@PathVariable teacherId: Long): List<UserTeacherCourseResponse> {
        return userService.getAllTeacherCourses(teacherId)
    }

    @GetMapping("/{userId}/children")
    fun getAllChildren(@PathVariable userId: Long): List<UserAccountsResponse> {
        return userService.getAllChildren(userId)
    }

    private fun User.toResponse(): UserRegisterResponse {
        return UserRegisterResponse(
            id = this.userId,
            email = this.email
        )
    }
}