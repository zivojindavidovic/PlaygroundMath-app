package rs.playgroundmath.playgroundmath.service

interface CommunicationService {

    fun sendUserRegistrationConfirmationEmail(email: String, confirmationLink: String)
}