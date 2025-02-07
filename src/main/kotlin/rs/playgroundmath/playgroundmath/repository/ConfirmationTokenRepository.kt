package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.model.ConfirmationToken

@Repository
interface ConfirmationTokenRepository: JpaRepository<ConfirmationToken, Long> {

    fun findByToken(token: String): ConfirmationToken?
}