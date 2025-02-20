package rs.playgroundmath.playgroundmath.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.common.Functions
import rs.playgroundmath.playgroundmath.exceptions.AccountMaximumPerUserException
import rs.playgroundmath.playgroundmath.exceptions.AccountNotFoundException
import rs.playgroundmath.playgroundmath.exceptions.DeleteAccountPasswordDoNotMatchException
import rs.playgroundmath.playgroundmath.exceptions.UserNotFoundException
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.AccountDeleteRequest
import rs.playgroundmath.playgroundmath.payload.request.AdminUpdateAccountPointsRequest
import rs.playgroundmath.playgroundmath.payload.response.*
import rs.playgroundmath.playgroundmath.repository.AccountRepository

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val userService: UserService
): AccountService {

    override fun createAccount(accountCreateRequest: AccountCreateRequest): AccountCreateResponse {
        val loggedInUserId = Functions.getCurrentLoggedInUserId()
            ?: throw UserNotFoundException("Korisnik sa kojim pokušavaš da kreiraš nalog ne postoji")

        val user = userService.findByUserId(loggedInUserId)

        if (countAccountsByUserId(user!!) >= 3) {
            throw AccountMaximumPerUserException("Ne možeš da registruješ više od 3 naloga po korisniku")
        }

        val account = accountRepository.save(Account(username = accountCreateRequest.username, age = accountCreateRequest.age, user = user))

        return account.toResponse()
    }

    override fun deleteAccount(accountDeleteRequest: AccountDeleteRequest) {
        val account = accountRepository.findById(accountDeleteRequest.accountId).orElseThrow {
            AccountNotFoundException("Account with id: ${accountDeleteRequest.accountId} not found")
        }

        val user = account.user ?: throw UserNotFoundException("User not found")

        val isPasswordValid = encoder().matches(accountDeleteRequest.userPassword, user.password)

        if (!isPasswordValid) {
            throw DeleteAccountPasswordDoNotMatchException("Uneta šifra nije ispravna. Nalog nije obrisan")
        }

        accountRepository.delete(account)
    }

    override fun getAccountsRelatedToUserId(userId: Long): AccountRelatedToUserResponse {
        val foundUser = userService.findByUserId(userId)
            ?: throw UserNotFoundException("Korisnik sa kojim pokušavaš da kreiraš nalog ne postoji")

        return accountRepository.findAllByUser(foundUser).toResponse()
    }

    override fun getRankList(): List<AccountRankListResponse> =
        accountRepository.findAllByOrderByPointsDesc().map {
            it.toRankListResponse()
        }

    override fun findByAccountId(accountId: Long): Account =
        accountRepository.findByAccountId(accountId)

    override fun getAccountPoints(accountId: Long): Long {
        return findByAccountId(accountId).points
    }

    override fun saveAccount(account: Account): Account =
        accountRepository.save(account)

    override fun getAllAccounts(): List<AccountResponse> {
        val accounts = accountRepository.findAll()

        return accounts.map { account ->
            AccountResponse(
                accountId = account.accountId,
                username = account.username,
                points = account.points
            )
        }
    }

    override fun updateAccountPoints(adminUpdateAccountPointsRequest: AdminUpdateAccountPointsRequest) {
        val account = findByAccountId(adminUpdateAccountPointsRequest.accountId)

        val updatedAccount = account.copy(
            points = adminUpdateAccountPointsRequest.points
        )

        saveAccount(updatedAccount)
    }

    override fun deleteAccountByAdmin(accountId: Long) {
        accountRepository.deleteById(accountId)
    }

    override fun getAccountById(accountId: Long): AccountResponse {
        return findByAccountId(accountId).toAccountResponse()
    }

    private fun Account.toAccountResponse(): AccountResponse =
        AccountResponse(
            accountId = this.accountId,
            username = this.username,
            points = this.points
        )

    private fun Account.toResponse(): AccountCreateResponse =
        AccountCreateResponse(
            id = this.accountId,
            username = this.username,
            userId = this.user!!.userId
        )

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

    private fun Account.toRankListResponse(): AccountRankListResponse =
        AccountRankListResponse(
            accountId = this.accountId,
            username = this.username,
            points = this.points
        )

    private fun countAccountsByUserId(user: User): Long {
        val currentUser = userService.findByUserId(user.userId)

        return accountRepository.countByUser(currentUser!!)
    }

    private fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
}