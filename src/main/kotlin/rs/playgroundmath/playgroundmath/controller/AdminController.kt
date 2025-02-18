package rs.playgroundmath.playgroundmath.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.payload.request.AdminUpdateAccountPointsRequest
import rs.playgroundmath.playgroundmath.payload.request.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.AccountResponse
import rs.playgroundmath.playgroundmath.payload.response.AdminUserResponse
import rs.playgroundmath.playgroundmath.service.AccountService
import rs.playgroundmath.playgroundmath.service.UserService

@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val accountService: AccountService,
    private val userService: UserService
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
}