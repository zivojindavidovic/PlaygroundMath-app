package rs.playgroundmath.playgroundmath.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class CommunicationServiceImpl(
    private val mailSender: JavaMailSender
): CommunicationService {

    override fun sendUserRegistrationConfirmationEmail(email: String, confirmationLink: String) {
        val mailMessage = SimpleMailMessage().apply {
            setTo(email)
            subject = "Confirm Your Registration"
            text = """
                        Welcome to MyApp!
                        
                        Please confirm your registration by clicking the link below:
                        $confirmationLink
                        
                        If you did not request this email, you can safely ignore it.
                        """.trimIndent()
        }

        mailSender.send(mailMessage)
    }
}