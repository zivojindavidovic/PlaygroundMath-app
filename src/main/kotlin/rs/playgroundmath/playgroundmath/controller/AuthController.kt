package rs.playgroundmath.playgroundmath.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.command.UserRegisterCommand
import rs.playgroundmath.playgroundmath.dto.UserRegisterDto
import rs.playgroundmath.playgroundmath.payload.request.RegisterRequest
import rs.playgroundmath.playgroundmath.payload.response.Response

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userRegisterCommand: UserRegisterCommand
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
}