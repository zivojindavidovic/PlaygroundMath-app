package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.model.Role
import rs.playgroundmath.playgroundmath.repository.RoleRepository

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {

    fun findRoleByRoleType(roleType: RoleType): Role? =
        roleRepository.findRoleByRoleType(roleType)
}