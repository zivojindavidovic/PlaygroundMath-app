package rs.playgroundmath.playgroundmath

import rs.playgroundmath.playgroundmath.dto.InputDto
import rs.playgroundmath.playgroundmath.validator.ValidationError
import rs.playgroundmath.playgroundmath.validator.Validator

abstract class AbstractCommand<T: InputDto, R> {
    protected var errors: List<ValidationError> = emptyList()

    abstract fun execute(dto: T): R?

    protected fun validate(validator: Validator<T>, dto: T): Boolean {
        errors = emptyList()
        val results = validator.validate(dto)

        if (results.isNotEmpty()) {
            errors = results
            return false
        }

        return true
    }

    fun getValidationErrors(): List<ValidationError> = errors
}