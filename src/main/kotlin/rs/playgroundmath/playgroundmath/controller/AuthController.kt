package rs.playgroundmath.playgroundmath.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.command.UserRegisterCommand
import rs.playgroundmath.playgroundmath.dto.UserRegisterDto
import rs.playgroundmath.playgroundmath.payload.request.AuthenticationRequest
import rs.playgroundmath.playgroundmath.payload.request.AuthenticationResponse
import rs.playgroundmath.playgroundmath.payload.request.RegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.Response
import rs.playgroundmath.playgroundmath.service.AuthenticationService

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userRegisterCommand: UserRegisterCommand,
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Any>
    {
        val userEmail = registerRequest.email
        val userPassword = registerRequest.password

        val userRegisterDto = UserRegisterDto(
            userEmail,
            userPassword
        )

        val results = userRegisterCommand.execute(userRegisterDto)

        val isValid = userRegisterCommand.getValidationErrors().isEmpty()

        val response = Response(
            success = userRegisterCommand.getValidationErrors().isEmpty(),
            errors = if (!isValid) userRegisterCommand.getValidationErrors() else emptyList(),
            results = if (isValid && results !== null) results else emptyList<Any>()
        )

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthenticationRequest): AuthenticationResponse =
        authenticationService.authentication(authRequest)

    @GetMapping("/test")
    fun test(): ResponseEntity<Any> {
        return ResponseEntity.ok("test")
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<String> {
        SecurityContextHolder.clearContext()
        return ResponseEntity.ok("Logged out")
    }
}