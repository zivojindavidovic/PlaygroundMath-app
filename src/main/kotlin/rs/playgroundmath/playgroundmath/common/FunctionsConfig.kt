package rs.playgroundmath.playgroundmath.common

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Configuration
class FunctionsConfig(
    private val userRepository: UserRepository
) {
    @PostConstruct
    fun initFunctions() {
        Functions.setUserRepository(userRepository)
    }
}
