package rs.playgroundmath.playgroundmath.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.common.Functions
import rs.playgroundmath.playgroundmath.exceptions.AccountMaximumPerUserException
import rs.playgroundmath.playgroundmath.exceptions.AccountNotFoundException
import rs.playgroundmath.playgroundmath.exceptions.UserNotFoundException
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.UpdateAccountRequest
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

    private fun Account.toResponse(): AccountCreateResponse =
        AccountCreateResponse(
            id = this.accountId,
            username = this.username,
            userId = this.user!!.userId
        )

    @Transactional
    fun deleteAccount(accountId: Long): AccountDeleteResponse {
        val account = accountRepository.findById(accountId).orElseThrow {
            AccountNotFoundException("Account with id: $accountId not found")
        }

        accountRepository.delete(account)

        return AccountDeleteResponse("Account ${account.username} deleted successfully")
    }

    fun updateAccount(updateAccountRequest: UpdateAccountRequest): UpdateAccountResponse {
        val foundAccount = accountRepository.findById(updateAccountRequest.accountId)

        if (foundAccount.isPresent) {
            val updatedAccount = foundAccount.get().copy(
                username = updateAccountRequest.username
            )

            accountRepository.save(updatedAccount)
        } else {
            throw AccountNotFoundException("Account ${foundAccount.get().accountId} not found")
        }

        return UpdateAccountResponse(foundAccount.get().accountId, updateAccountRequest.username, foundAccount.get().points)
    }

    fun getAccountsRelatedToUserId(userId: Long): List<Account> {
        val foundUser = userService.findByUserId(userId)
            ?: throw UserNotFoundException("Korisnik sa kojim pokušavaš da kreiraš nalog ne postoji")

        return accountRepository.findAllByUser(foundUser)
    }

    fun getRankList(): List<AccountRankListResponse> =
        accountRepository.findAllByOrderByPointsDesc().map {
            it.toRankListResponse()
        }

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
}