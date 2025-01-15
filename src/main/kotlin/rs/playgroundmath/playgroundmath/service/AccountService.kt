package rs.playgroundmath.playgroundmath.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.exceptions.AccountMaximumPerUserException
import rs.playgroundmath.playgroundmath.model.Account
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.AccountCreateRequest
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