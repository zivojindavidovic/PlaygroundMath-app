package rs.playgroundmath.playgroundmath.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.AccountDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.service.AccountService

@RestController
@RequestMapping("/api/v1/account")
class AccountController(
    private val accountService: AccountService,
) {

    @PostMapping("/create")
    fun createAccount(@Valid @RequestBody accountCreateRequest: AccountCreateRequest): ResponseEntity<ApiResponse<AccountCreateResponse>> {
        val result = accountService.createAccount(accountCreateRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(result)
            )
        )
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody accountDeleteRequest: AccountDeleteRequest): ResponseEntity<ApiResponse<Any>> {
        accountService.deleteAccount(accountDeleteRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf()
            )
        )
    }

    @GetMapping("/getAll")
    fun getAccountsRelatedToUserId(@RequestParam("userId") userId: Long): ResponseEntity<ApiResponse<AccountRelatedToUserResponse>> {
        val results = accountService.getAccountsRelatedToUserId(userId)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(results)
            )
        )
    }

    @GetMapping("/rankList")
    fun getRankList(): List<AccountRankListResponse> =
        accountService.getRankList()

    @GetMapping("/get")
    fun getById(@RequestParam("accountId") accountId: Long): AccountResponse =
        accountService.getAccountById(accountId)
}

