package rs.playgroundmath.playgroundmath.payload.request

data class GenerateTasksRequest(
    val numberOneFrom: Long,
    val numberOneTo: Long,
    val numberTwoFrom: Long,
    val numberTwoTo: Long,
    val operations: MutableList<String> = mutableListOf("+"),
    val testType: String = "pdf",
    val accountId: Long? = null,
    val courseId: Long? = null,
    val sumUnitsGoesOverCurrentTenSum: Boolean = false,
    val sumExceedTwoDigitsSum: Boolean = false,
    val allowedNegativeResultsSub: Boolean = false,
    val allowedBiggerUnitsInSecondNumberSub: Boolean = false,
    val allowedThreeDigitsResultMul: Boolean = false
)
