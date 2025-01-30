package rs.playgroundmath.playgroundmath.payload.response

data class PdfTaskResponse(
    val type: String = "pdf",
    val tasks: List<String>
)
