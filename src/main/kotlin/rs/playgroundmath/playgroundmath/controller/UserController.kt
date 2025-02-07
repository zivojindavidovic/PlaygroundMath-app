package rs.playgroundmath.playgroundmath.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.request.working.ApiResponse
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

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody userRegisterRequest: UserRegisterRequest): ResponseEntity<ApiResponse<UserRegisterResponse>> {
        val result = userService.registerUser(userRegisterRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(result)
            )
        )
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
}