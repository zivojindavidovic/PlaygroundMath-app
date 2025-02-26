package rs.playgroundmath.playgroundmath.payload.response

data class TeacherCourseInformationTestsResponse(
    val testId: Long,
    val isCompleted: Boolean = false,
    val tasks: List<TaskResponse>
)
