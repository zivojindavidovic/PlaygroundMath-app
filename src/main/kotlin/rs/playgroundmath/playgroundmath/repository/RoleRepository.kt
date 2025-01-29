package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.model.Role

@Repository
interface RoleRepository: JpaRepository<Role, Long> {

    fun findRoleByRoleType(roleType: RoleType): Role?
}