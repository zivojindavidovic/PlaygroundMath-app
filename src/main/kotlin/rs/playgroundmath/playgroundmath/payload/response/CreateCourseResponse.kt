package rs.playgroundmath.playgroundmath.payload.response

import java.time.LocalDateTime

data class CreateCourseResponse(
    val courseId: Long,
    val age: Long,
    val dueDate: LocalDateTime
)
