package rs.playgroundmath.playgroundmath.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import rs.playgroundmath.playgroundmath.exceptions.AccountMaximumPerUserException
import rs.playgroundmath.playgroundmath.exceptions.UserAlreadyExistsException
import rs.playgroundmath.playgroundmath.payload.request.working.ApiResponse

//@RestControllerAdvice
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<ApiResponse<Any>> {
        val errors = listOf(mapOf("email" to (ex.message ?: "Invalid value")))

        val response = ApiResponse<Any>(
            success = false,
            errors = errors,
            results = emptyList()
        )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
//
//    @ExceptionHandler(AccountMaximumPerUserException::class)
//    fun handleAccountMaximumPerUserException(ex: AccountMaximumPerUserException): ResponseEntity<Map<String, String>> {
//        val response: Map<String, String> = mapOf(
//            "error" to "Account Maximum Per User",
//            "message" to (ex.message ?: "Account maximum per user reached")
//        )
//
//        return ResponseEntity(response, HttpStatus.CONFLICT)
//    }
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Any>> {
        val errors = ex.bindingResult.fieldErrors.map { fieldError ->
            mapOf(fieldError.field to (fieldError.defaultMessage ?: "Invalid value"))
        }

        val response = ApiResponse<Any>(
            success = false,
            errors = errors,
            results = emptyList()
        )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}
