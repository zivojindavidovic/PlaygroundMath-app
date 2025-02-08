package rs.playgroundmath.playgroundmath.payload.response

data class TaskGenerateResponse(
    val type: String = "pdf",
    val tasks: List<String>? = null
)
