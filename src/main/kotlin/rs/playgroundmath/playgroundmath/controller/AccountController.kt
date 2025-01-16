package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.UpdateAccountRequest
import rs.playgroundmath.playgroundmath.payload.response.*
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

    @DeleteMapping("/delete/{accountId}")
    fun delete(@PathVariable accountId: Long): AccountDeleteResponse =
        accountService.deleteAccount(accountId)

    @PutMapping("/update/{accountId}")
    fun update(@RequestBody updateAccountRequest: UpdateAccountRequest): UpdateAccountResponse =
        accountService.updateAccount(updateAccountRequest)

    @GetMapping("/user/{userId}")
    fun getAccountsRelatedToUserId(@PathVariable userId: Long) =
        accountService.getAccountsRelatedToUserId(userId).toResponse()

    private fun List<Account>.toResponse(): AccountRelatedToUserResponse =
        AccountRelatedToUserResponse(
            accounts = this.map { account ->
                AccountResponse(
                    accountId = account.accountId,
                    username = account.username,
                    points = account.points
                )
            }
        )


    private fun Account.toResponse(): AccountCreateResponse =
        AccountCreateResponse(
            id = this.accountId,
            username = this.username,
            userId = this.user!!.userId
        )
}

