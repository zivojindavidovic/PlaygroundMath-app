package rs.playgroundmath.playgroundmath.command

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.AbstractCommand
import rs.playgroundmath.playgroundmath.dto.UserLoginCommandResultDto
import rs.playgroundmath.playgroundmath.dto.UserLoginDto

@Service
class UserLoginCommand(): AbstractCommand<UserLoginDto, UserLoginCommandResultDto>() {

    override fun execute(dto: UserLoginDto): UserLoginCommandResultDto? {
        return login(dto)
    }

    protected fun login(dto: UserLoginDto): UserLoginCommandResultDto?
    {
        return null
    }
}