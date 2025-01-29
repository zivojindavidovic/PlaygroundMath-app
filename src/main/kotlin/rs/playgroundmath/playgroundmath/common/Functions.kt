package rs.playgroundmath.playgroundmath.common

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.repository.UserRepository

class Functions {
    companion object {
        private lateinit var userRepository: UserRepository

        fun setUserRepository(repo: UserRepository) {
            userRepository = repo
        }

        fun isAdmin(): Boolean {
            val currentUser = SecurityContextHolder.getContext().authentication.principal as UserDetails
            val user = findUserByEmail(currentUser)

            return user != null && user.role.roleType == RoleType.ADMIN
        }

        fun getCurrentLoggedInUserId(): Long? {
            val currentUser = SecurityContextHolder.getContext().authentication.principal as UserDetails
            val user = findUserByEmail(currentUser)

            if (user != null) {
                return user.userId
            }

            return null
        }

        private fun findUserByEmail(userDetails: UserDetails): User? =
            userRepository.findByEmail(userDetails.username)
    }
}