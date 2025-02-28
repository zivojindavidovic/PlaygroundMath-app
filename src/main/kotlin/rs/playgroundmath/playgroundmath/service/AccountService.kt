package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.AccountDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.AdminUpdateAccountPointsRequest
import rs.playgroundmath.playgroundmath.payload.response.*

interface AccountService {

    fun createAccount(accountCreateRequest: AccountCreateRequest): AccountCreateResponse

    fun findByAccountId(accountId: Long): Account

    fun getAccountPoints(accountId: Long): Long

    fun saveAccount(account: Account): Account

    fun getAccountsRelatedToUserId(userId: Long): AccountRelatedToUserResponse

    fun deleteAccount(accountDeleteRequest: AccountDeleteRequest)

    fun getRankList(): List<AccountRankListResponse>

    fun getAllAccounts(): List<AdminAccountResponse>

    fun updateAccountByAdmin(adminUpdateAccountPointsRequest: AdminUpdateAccountPointsRequest)

    fun deleteAccountByAdmin(accountId: Long)

    fun getAccountById(accountId: Long): AccountResponse
}