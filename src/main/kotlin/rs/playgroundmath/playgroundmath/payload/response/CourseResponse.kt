package rs.playgroundmath.playgroundmath.payload.response

import java.time.LocalDateTime

data class CourseResponse(
    val courseId: Long,
    val age: Long,
    val dueDate: LocalDateTime,
    val title: String,
    val description: String
)
