package rs.playgroundmath.playgroundmath.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import rs.playgroundmath.playgroundmath.exceptions.AccountMaximumPerUserException
import rs.playgroundmath.playgroundmath.exceptions.UserAlreadyExistsException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<Map<String, String>> {
        val response: Map<String, String> = mapOf(
            "error" to "User Already Exists",
            "message" to (ex.message ?: "User with this email already exists")
        )

        return ResponseEntity(response, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(AccountMaximumPerUserException::class)
    fun handleAccountMaximumPerUserException(ex: AccountMaximumPerUserException): ResponseEntity<Map<String, String>> {
        val response: Map<String, String> = mapOf(
            "error" to "Account Maximum Per User",
            "message" to (ex.message ?: "Account maximum per user reached")
        )

        return ResponseEntity(response, HttpStatus.CONFLICT)
    }
}
