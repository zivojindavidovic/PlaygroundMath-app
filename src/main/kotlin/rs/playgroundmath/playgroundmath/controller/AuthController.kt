package rs.playgroundmath.playgroundmath.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.payload.request.AuthenticationRequest
import rs.playgroundmath.playgroundmath.payload.request.ApiResponse
import rs.playgroundmath.playgroundmath.payload.response.AuthenticationResponse
import rs.playgroundmath.playgroundmath.service.AuthenticationServiceImpl

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationService: AuthenticationServiceImpl
) {

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthenticationRequest): ResponseEntity<ApiResponse<AuthenticationResponse>> {
        val result = authenticationService.authenticate(authRequest)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                errors = emptyList(),
                results = listOf(result)
            )
        )
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<String> {
        SecurityContextHolder.clearContext()
        return ResponseEntity.ok("Logged out")
    }
}