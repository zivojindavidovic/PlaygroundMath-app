package rs.playgroundmath.playgroundmath.payload.response

import java.time.LocalDateTime

data class CourseResponse(
    val courseId: Long,
    val dueDate: LocalDateTime?,
    val age: Long
)
