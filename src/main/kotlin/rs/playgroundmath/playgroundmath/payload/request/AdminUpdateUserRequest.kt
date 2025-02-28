package rs.playgroundmath.playgroundmath.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class AdminUpdateUserRequest(
    val userId: Long,
    @field:NotBlank(message = "Ime mora biti uneto")
    @field:Size(min = 2, message = "Ime mora imati najmanje 2 karaktera")
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "Ime mora sadrzati samo slova")
    val firstName: String,
    @field:NotBlank(message = "Prezime mora biti uneto")
    @field:Size(min = 2, message = "Prezime mora imati najmanje 2 karaktera")
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "Prezime mora sadrzati samo slova")
    val lastName: String
)
