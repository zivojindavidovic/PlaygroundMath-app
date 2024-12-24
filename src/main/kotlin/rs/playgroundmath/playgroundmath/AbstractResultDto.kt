package rs.playgroundmath.playgroundmath

class AbstractResultDto(
    var success: Boolean = false,
    var errors: List<Map<String, String>> = emptyList(),
) {
    fun setFail()
    {
        this.success = false
    }

    fun setSuccess()
    {
        this.success = true
    }
}