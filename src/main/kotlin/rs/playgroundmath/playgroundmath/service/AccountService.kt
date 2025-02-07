package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.response.AccountCreateResponse

interface AccountService {

    fun createAccount(accountCreateRequest: AccountCreateRequest): AccountCreateResponse
}