package rs.playgroundmath.playgroundmath.payload.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class CourseCreateRequest(
    @field:NotBlank(message = "User ID is required")
    val userId: Long,

    @field:NotNull(message = "Moraš uneti godine kako bi kreirao kurs")
    @field:Min(value = 3, message = "Kurs može biti namenjen samo deci starijoj od 3 godine")
    @field:Max(value = 10, message = "Kurs može biti namenjen samo deci mlađoj od 10 godina")
    val age: Long,

    @field:NotNull(message = "Moraš uneti datum kada se kurs završava kako bi kreirao kurs")
    val dueDate: LocalDateTime,

    @field:NotBlank(message = "Moraš uneti naslov kako bi kreirao kurs")
    @field:Min(value = 3, message = "Naslov mora biti duži od 3 karaktera")
    val title: String,

    @field:NotBlank(message = "Moraš uneti opis kako bi kreirao kurs")
    @field:Min(value = 3, message = "Opis mora biti duži od 3 karaktera")
    val description: String
)
