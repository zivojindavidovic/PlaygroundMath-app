package rs.playgroundmath.playgroundmath.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.config.JwtProperties
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.enums.Status
import rs.playgroundmath.playgroundmath.exceptions.UserStatusNotActiveException
import rs.playgroundmath.playgroundmath.payload.request.AuthenticationRequest
import rs.playgroundmath.playgroundmath.payload.response.AuthenticationResponse
import java.util.*

@Service
class AuthenticationServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val userService: UserServiceImpl
): AuthenticationService {

    override fun authenticate(authRequest: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(authRequest.email)
        val email = user.username
        val applicationUser = userService.findByEmail(email)

        if (applicationUser!!.status == Status.PENDING) {
            throw UserStatusNotActiveException("Nalog sa ovom e adresom nije aktivan")
        }

        val accessToken = tokenService.generateToken(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
            additionalClaims = mapOf("role" to applicationUser.role.roleType.toString())
        )

        val loggedInUser = userService.findByEmail(user.username)
        val isTeacher = loggedInUser!!.role.roleType == RoleType.TEACHER

        return AuthenticationResponse(
            accessToken = accessToken,
            userId = loggedInUser.userId,
            email = loggedInUser.email,
            isTeacher = isTeacher
        )
    }
}