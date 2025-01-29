package rs.playgroundmath.playgroundmath.payload.request

import java.time.LocalDateTime

data class CourseCreateRequest(
    val userId: Long,
    val age: Long,
    val dueDate: LocalDateTime
)
