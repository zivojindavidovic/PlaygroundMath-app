package rs.playgroundmath.playgroundmath.command

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.AbstractCommand
import rs.playgroundmath.playgroundmath.dto.UserRegisterDto
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.repository.UserRepository
import rs.playgroundmath.playgroundmath.validator.UserRegisterDtoValidator

@Service
class UserRegisterCommand(
    private val userRepository: UserRepository,
    private val validator: UserRegisterDtoValidator
): AbstractCommand<UserRegisterDto, User>() {
    override fun execute(dto: UserRegisterDto): User?
    {
        if (!validate(validator, dto)) {
            return null;
        }

        val user = User(
            email = dto.email,
            password = dto.password
        )

        return userRepository.save(user)
    }
}