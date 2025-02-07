package rs.playgroundmath.playgroundmath.payload.request

import jakarta.validation.constraints.*

data class AccountCreateRequest(
    @field:NotBlank(message = "Ime naloga ne može da bude prazno")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9]+$",
        message = "Korisničko ime može da sadrži samo slova i brojeve"
    )
    @field:Size(min = 3, max = 20, message = "Korisničko ime mora imati između 3 i 20 karaktera")
    val username: String,

    @field:NotNull(message = "Moraš uneti godine deteta")
    @field:Min(value = 3, message = "Dete može imati minimalno 3 godine")
    @field:Max(value = 10, message = "Dete može imati maksimalno 10 godina")
    val age: Long
)
