package rs.playgroundmath.playgroundmath.payload.request

import rs.playgroundmath.playgroundmath.model.RoleType

data class UserRegisterRequest(
    val email: String,
    val password: String,
    val accountType: RoleType = RoleType.PARENT
)