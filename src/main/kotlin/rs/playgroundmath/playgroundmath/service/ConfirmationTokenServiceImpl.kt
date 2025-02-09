package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.model.ConfirmationToken
import rs.playgroundmath.playgroundmath.model.User
import rs.playgroundmath.playgroundmath.repository.ConfirmationTokenRepository
import java.time.LocalDateTime
import java.util.*

@Service
class ConfirmationTokenServiceImpl(
    private val confirmationTokenRepository: ConfirmationTokenRepository
): ConfirmationTokenService {

    override fun createConfirmationToken(user: User): ConfirmationToken {
        val token = UUID.randomUUID().toString()
        val expiration = LocalDateTime.now().plusHours(24)

        val confirmationToken = ConfirmationToken(
            token = token,
            userId = user.userId,
            expiresAt = expiration
        )

        return confirmationTokenRepository.save(confirmationToken)
    }

    override fun findTokenByToken(token: String): ConfirmationToken? {
        return confirmationTokenRepository.findByToken(token)
    }
}