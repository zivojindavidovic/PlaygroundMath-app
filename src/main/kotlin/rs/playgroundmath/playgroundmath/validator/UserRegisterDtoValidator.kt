package rs.playgroundmath.playgroundmath.validator

import org.springframework.stereotype.Component
import rs.playgroundmath.playgroundmath.dto.UserRegisterDto
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Component
class UserRegisterDtoValidator(
    private val userRepository: UserRepository
): Validator<UserRegisterDto> {
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

        if (emailExists(input.email)) {
            errors.add(ValidationError("email", "User with email already exists"))
        }

        return errors;
    }

    protected fun emailExists(email: String): Boolean
    {
        return userRepository.existsByEmail(email);
    }
}