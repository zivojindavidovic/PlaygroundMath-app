package rs.playgroundmath.playgroundmath.validator

import org.springframework.stereotype.Component
import rs.playgroundmath.playgroundmath.dto.UserRegisterDto

@Component
class UserRegisterDtoValidator: Validator<UserRegisterDto> {
    override fun validate(input: UserRegisterDto): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        if (input.password.length < 7) {
            errors.add(ValidationError("password", "Password has to be at least 7 characters"))
        }

        if (input.password.isEmpty()) {
            errors.add(ValidationError("password", "Password cannot be empty"))
        }

        if (input.email.isEmpty()) {
            errors.add(ValidationError("email", "Email cannot be empty"))
        }

        return errors;
    }
}