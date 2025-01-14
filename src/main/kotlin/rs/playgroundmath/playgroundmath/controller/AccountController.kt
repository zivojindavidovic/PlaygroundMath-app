package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.response.AccountCreateResponse
import rs.playgroundmath.playgroundmath.service.AccountService
@RestController
@RequestMapping("/api/v1/account")
class AccountController(
    private val accountService: AccountService,
) {

    @PostMapping("/create")
    fun create(@RequestBody accountCreateRequest: AccountCreateRequest): AccountCreateResponse {
        val createdAccount = accountService.createAccount(accountCreateRequest)

        return createdAccount.toResponse()
    }

    private fun Account.toResponse(): AccountCreateResponse =
        AccountCreateResponse(
            id = this.accountId,
            username = this.username,
            userId = this.user!!.userId
        )
}

