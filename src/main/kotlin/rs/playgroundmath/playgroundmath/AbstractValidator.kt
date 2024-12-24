package rs.playgroundmath.playgroundmath

class AbstractValidator {
    protected val errors: Array<Map<String, String>> = emptyArray()

    fun validate(): Array<Map<String, String>>
    {
        return errors
    }
}