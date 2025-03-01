package rs.playgroundmath.playgroundmath.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.payload.request.UserDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.payload.request.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.*
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
    fun getUserAccountCourses(@RequestParam userId: Long): UserAccountCoursesResponse =
        userService.getUserAccountCourses(userId)

    @GetMapping("/accounts/{accountId}/tests")
    fun getUserAccountTests(@PathVariable accountId: Long, @RequestParam courseId: Long): AccountTestsResponse =
        userService.getUserAccountTests(accountId, courseId)

    @GetMapping("/teachers/{teacherId}/courses/{courseId}")
    fun getCourseInformation(@PathVariable teacherId: Long, @PathVariable courseId: Long): TeacherCourseInformationResponse =
        userService.getTeacherCourseInformation(teacherId, courseId)

    @GetMapping
    fun getById(@RequestParam("userId") userId: Long) =
        userService.getById(userId)
}
