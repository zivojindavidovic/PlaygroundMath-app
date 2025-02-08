package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.response.AccountCreateResponse

interface AccountService {

    fun createAccount(accountCreateRequest: AccountCreateRequest): AccountCreateResponse

    fun findByAccountId(accountId: Long): Account

    fun getAccountPoints(accountId: Long): Long

    fun saveAccount(account: Account): Account
}