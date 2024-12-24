package rs.playgroundmath.playgroundmath.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.AbstractCommand
import rs.playgroundmath.playgroundmath.AbstractResultDto
import rs.playgroundmath.playgroundmath.dto.InputDto
import rs.playgroundmath.playgroundmath.dto.UserRegisterDto
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Service
class UserRegisterCommand(
    private val userRepository: UserRepository
): AbstractCommand() {
    protected var dto: UserRegisterDto = UserRegisterDto()

    override fun execute(dto: InputDto): AbstractResultDto
    {
        this.dto = dto as UserRegisterDto

        if (validate()) {
            registerUser()
        }

        return generateResults()
    }

    protected fun registerUser(): User {
        val user = User(
            email = this.dto.email,
            password = this.dto.password
        )

        return userRepository.save(user)
    }
}