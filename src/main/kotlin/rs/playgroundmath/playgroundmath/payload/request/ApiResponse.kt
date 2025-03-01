package rs.playgroundmath.playgroundmath.payload.request

data class ApiResponse<T>(
    val success: Boolean,
    val errors: List<Map<String, String>> = emptyList(),
    val results: List<T>? = emptyList()
)
