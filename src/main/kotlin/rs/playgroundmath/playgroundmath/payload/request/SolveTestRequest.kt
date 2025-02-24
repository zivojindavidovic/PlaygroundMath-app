package rs.playgroundmath.playgroundmath.payload.request

data class SolveTestRequest(
    val testAnswers: List<Map<Long, String>>,
    val accountId: Long,
    val testId: Long? = null
)
