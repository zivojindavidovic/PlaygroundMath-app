package rs.playgroundmath.playgroundmath.service

import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.exceptions.AccountMaximumPerUserException
import rs.playgroundmath.playgroundmath.exceptions.AccountNotFoundException
import rs.playgroundmath.playgroundmath.exceptions.UserNotFoundException
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
import rs.playgroundmath.playgroundmath.payload.request.UpdateAccountRequest
import rs.playgroundmath.playgroundmath.payload.response.AccountDeleteResponse
import rs.playgroundmath.playgroundmath.payload.response.UpdateAccountResponse
import rs.playgroundmath.playgroundmath.repository.AccountCourseRepository
import rs.playgroundmath.playgroundmath.repository.AccountRepository
import rs.playgroundmath.playgroundmath.repository.TestRepository
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val testRepository: TestRepository,
    private val accountCourseRepository: AccountCourseRepository
) {

    fun createAccount(accountCreateRequest: AccountCreateRequest): Account {
        if (countAccountsByUserId() == null) {
            throw AccountMaximumPerUserException("User not found")
        }

        if (countAccountsByUserId()!! >= 3) {
            throw AccountMaximumPerUserException("Account maximum per user reached")
        }

        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val user = findUserByEmail(currentUser)

        val account = accountRepository.save(Account(username = accountCreateRequest.username, age = accountCreateRequest.age, user = user))
        return account
    }

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
        val foundUser = userRepository.findById(userId)

        return if (foundUser.isPresent) {
            val user = foundUser.get()

            accountRepository.findAllByUser(user)
        } else {
            throw UserNotFoundException("User with $userId not found")
        }
    }

    private fun countAccountsByUserId(): Long? {
        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserDetails

        val user = findUserByEmail(currentUser)
        return if (user != null)
            accountRepository.countByUser(user)
        else
            null

    }

    private fun findUserByEmail(userDetails: UserDetails): User? =
        userRepository.findByEmail(userDetails.username)


}