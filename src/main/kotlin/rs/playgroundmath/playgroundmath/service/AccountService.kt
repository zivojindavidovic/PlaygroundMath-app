package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.AccountDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.AdminUpdateAccountPointsRequest
import rs.playgroundmath.playgroundmath.payload.response.AccountCreateResponse
import rs.playgroundmath.playgroundmath.payload.response.AccountRankListResponse
import rs.playgroundmath.playgroundmath.payload.response.AccountRelatedToUserResponse
import rs.playgroundmath.playgroundmath.payload.response.AccountResponse

interface AccountService {

    fun createAccount(accountCreateRequest: AccountCreateRequest): AccountCreateResponse

    fun findByAccountId(accountId: Long): Account

    fun getAccountPoints(accountId: Long): Long

    fun saveAccount(account: Account): Account

    fun getAccountsRelatedToUserId(userId: Long): AccountRelatedToUserResponse

    fun deleteAccount(accountDeleteRequest: AccountDeleteRequest)

    fun getRankList(): List<AccountRankListResponse>

    fun getAllAccounts(): List<AccountResponse>

    fun updateAccountPoints(adminUpdateAccountPointsRequest: AdminUpdateAccountPointsRequest)

    fun deleteAccountByAdmin(accountId: Long)
}