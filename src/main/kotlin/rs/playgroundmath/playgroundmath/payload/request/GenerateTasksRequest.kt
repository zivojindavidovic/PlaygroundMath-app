package rs.playgroundmath.playgroundmath.payload.request

data class GenerateTasksRequest(
    val numberOneFrom: Long,
    val numberOneTo: Long,
    val numberTwoFrom: Long,
    val numberTwoTo: Long,
    val operations: MutableList<String> = mutableListOf("+"),
    val testType: String = "pdf",
    val accountId: Long = 0
)
