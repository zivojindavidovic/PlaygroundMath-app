package rs.playgroundmath.playgroundmath.payload.response

data class TeacherCourseInformationResponse(
    val courseId: Long,
    val totalTests: Long,
    val accounts: List<TeacherCourseInformationAccountsResponse>,
    val tests: List<TeacherCourseInformationTestsResponse>
)
