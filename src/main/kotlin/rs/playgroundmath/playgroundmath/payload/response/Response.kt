package rs.playgroundmath.playgroundmath.payload.response

class Response(
    var success: Boolean = false,
    var errors: Any = emptyList<Any>(),
    var results: Any = emptyList<Any>()
)
