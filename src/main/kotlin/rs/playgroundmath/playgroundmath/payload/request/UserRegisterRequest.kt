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
    val accountType: RoleType = RoleType.PARENT,
    @field:NotBlank(message = "Ime mora biti uneto")
    @field:Size(min = 2, message = "Ime mora imati najmanje 2 karaktera")
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "Ime mora sadrzati samo slova")
    val firstName: String,
    @field:NotBlank(message = "Prezime mora biti uneto")
    @field:Size(min = 2, message = "Prezime mora imati najmanje 2 karaktera")
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "Prezime mora sadrzati samo slova")
    val lastName: String
)