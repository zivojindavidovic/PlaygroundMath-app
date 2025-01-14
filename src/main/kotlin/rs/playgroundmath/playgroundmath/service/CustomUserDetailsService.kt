package rs.playgroundmath.playgroundmath.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.repository.UserRepository

typealias ApplicationUser = rs.playgroundmath.playgroundmath.model.User

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String?): UserDetails =
        userRepository.findByEmail(email)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("User with email $email not found")

    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.email)
            .password(this.password)
            .build()
}