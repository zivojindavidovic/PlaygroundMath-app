package rs.playgroundmath.playgroundmath.payload.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import rs.playgroundmath.playgroundmath.enums.RoleType

data class UserRegisterRequest(
    @field:NotBlank(message = "E-adresa mora biti uneta")
    @field:Email(message = "E-adresa mora biti u ispravnom formatu")
    val email: String,
    @field:NotBlank(message = "Lozinka mora biti uneta")
    @field:Size(min = 5, message = "Lozinka mora imati najmanje 5 karaktera")
    @field:Pattern(
        regexp = "^(?=.*\\d)(?=.*[!@#\$%^&*._+=-]).+$",
        message = "Lozinka mora sadrzati barem jedan broj i barem jedan specijalni karakter"
    )
    val password: String,
    val accountType: RoleType = RoleType.PARENT
)