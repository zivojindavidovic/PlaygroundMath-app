package rs.playgroundmath.playgroundmath.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.exceptions.UserAlreadyExistsException
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.payload.request.UserRegisterRequest
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(userRegisterRequest: UserRegisterRequest): User {
        val foundUser = userRepository.findByEmail(userRegisterRequest.email)

        if (foundUser != null) {
            throw UserAlreadyExistsException("User with email ${userRegisterRequest.email} already exists")
        }

        return userRepository.save(User(email = userRegisterRequest.email, password = encoder().encode(userRegisterRequest.password)))
    }

    private fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
}