package rs.playgroundmath.playgroundmath.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.payload.request.UserDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.request.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.UserAccountCoursesResponse
import rs.playgroundmath.playgroundmath.payload.response.UserRegisterResponse
import rs.playgroundmath.playgroundmath.payload.response.UserTeachersResponse
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

    @PostMapping("/confirm")
    fun confirmRegistration(@RequestBody token: String): ResponseEntity<Boolean> =
        ResponseEntity.ok(userService.confirmUserRegistration(token))


    @DeleteMapping("/delete")
    fun delete(@RequestBody userDeleteRequest: UserDeleteRequest): ResponseEntity<ApiResponse<Any>> {
        userService.deleteUser(userDeleteRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = emptyList()
            )
        )
    }

    @GetMapping("/teachers")
    fun getAllTeachers(): List<UserTeachersResponse> {
        return userService.getAllTeachers()
    }

    @GetMapping("/accounts/courses")
    fun getUserAccountCourses(@RequestParam userId: Long): List<UserAccountCoursesResponse> =
        userService.getUserAccountCourses(userId)
}
