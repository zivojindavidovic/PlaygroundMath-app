package rs.playgroundmath.playgroundmath.service

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
import rs.playgroundmath.playgroundmath.repository.AccountRepository
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
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

        val account = accountRepository.save(Account(username = accountCreateRequest.username, user = user))
        return account
    }

    fun deleteAccount(accountId: Long): AccountDeleteResponse {
        val foundAccount = accountRepository.findById(accountId)

        if (foundAccount.isPresent) {
            accountRepository.delete(foundAccount.get())
        } else {
            throw AccountNotFoundException("Account with id: $accountId now found")
        }

        return AccountDeleteResponse("Account ${foundAccount.get().username} delete successfully")
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