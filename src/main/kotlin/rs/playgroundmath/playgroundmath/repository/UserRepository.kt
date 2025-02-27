package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.model.Role
import rs.playgroundmath.playgroundmath.model.User

interface UserRepository: JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String?): User?

    fun findAllByRole_RoleId(roleId: Long): List<User>

    fun findByUserId(userId: Long): User?

    fun findAllByRole_RoleTypeNot(roleType: RoleType): List<User>

    fun findAllByRole(role: Role): List<User>
}