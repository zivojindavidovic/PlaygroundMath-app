package rs.playgroundmath.playgroundmath.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rs.playgroundmath.playgroundmath.payload.request.AdminUpdateAccountPointsRequest
import rs.playgroundmath.playgroundmath.payload.request.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.AccountResponse
import rs.playgroundmath.playgroundmath.payload.response.AdminTeacherResponse
import rs.playgroundmath.playgroundmath.payload.response.AdminUserResponse
import rs.playgroundmath.playgroundmath.service.AccountService
import rs.playgroundmath.playgroundmath.service.AdminService
import rs.playgroundmath.playgroundmath.service.UserService

@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val accountService: AccountService,
    private val userService: UserService,
    private val adminService: AdminService
) {

    @GetMapping("/accounts")
    fun getAllAccount(): List<AccountResponse> =
        accountService.getAllAccounts()

    @GetMapping("/users")
    fun getAllUsers(): List<AdminUserResponse> =
        userService.getAll()

    @PostMapping("/update-account-points")
    fun updateAccountPoints(@RequestBody adminUpdateAccountPointsRequest: AdminUpdateAccountPointsRequest): ResponseEntity<ApiResponse<Any>> {
        val result = accountService.updateAccountPoints(adminUpdateAccountPointsRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(result)
            )
        )
    }

    @DeleteMapping("/delete-account")
    fun deleteAccount(@RequestParam accountId: Long): ResponseEntity<ApiResponse<Any>>
    {
        accountService.deleteAccountByAdmin(accountId)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = emptyList()
            )
        )
    }

    @DeleteMapping("/delete-user")
    fun deleteUser(@RequestParam userId: Long): ResponseEntity<ApiResponse<Any>>
    {
        userService.deleteUserByAdmin(userId)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = emptyList()
            )
        )
    }

    @GetMapping("/teacher-courses")
    fun getTeacherCourses(): List<AdminTeacherResponse> =
        adminService.getTeacherInformation()

    @DeleteMapping("/course/{courseId}")
    fun deleteCourseByAdmin(@PathVariable courseId: Long): Boolean =
        adminService.deleteCourseByAdmin(courseId)

    @DeleteMapping("/test/{testId}")
    fun deleteTestByAdmin(@PathVariable testId: Long): Boolean =
        adminService.deleteTestByAdmin(testId)
}