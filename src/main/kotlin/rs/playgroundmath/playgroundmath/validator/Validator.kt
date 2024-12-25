package rs.playgroundmath.playgroundmath.validator

import rs.playgroundmath.playgroundmath.dto.InputDto

interface Validator<T> {
    fun validate(input: T): List<ValidationError>
}
