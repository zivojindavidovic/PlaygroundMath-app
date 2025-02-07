package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.ConfirmationToken
import rs.playgroundmath.playgroundmath.model.User

interface ConfirmationTokenService {

    fun createConfirmationToken(user: User): ConfirmationToken
}