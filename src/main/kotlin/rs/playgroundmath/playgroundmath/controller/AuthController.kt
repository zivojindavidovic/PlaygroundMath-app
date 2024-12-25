package rs.playgroundmath.playgroundmath.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.playgroundmath.playgroundmath.command.UserRegisterCommand
import rs.playgroundmath.playgroundmath.dto.UserRegisterDto
import rs.playgroundmath.playgroundmath.payload.request.RegisterRequest

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

        return if (userRegisterCommand.getValidationErrors().isNotEmpty()) {
            ResponseEntity.badRequest().body(userRegisterCommand.getValidationErrors())
        } else {
            ResponseEntity.ok(results)
        }
    }
}