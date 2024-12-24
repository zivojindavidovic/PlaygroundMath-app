package rs.playgroundmath.playgroundmath

import rs.playgroundmath.playgroundmath.dto.InputDto

abstract class AbstractCommand {
    protected var errors: Array<Map<String, String>> = emptyArray()

    abstract fun execute(dto: InputDto): AbstractResultDto

    protected fun validate(): Boolean
    {
        val validator = getValidatorInstance()
        val results = validator.validate()

        val isValidationSuccess = results.isEmpty()

        if (!isValidationSuccess) {
            errors = results
        }

        return isValidationSuccess
    }

    protected fun getValidatorInstance(): AbstractValidator
    {
        return AbstractValidator()
    }

    protected fun handleErrorResults(result: Any)
    {

    }

    protected fun getResultDtoInstance(): AbstractResultDto
    {
        return AbstractResultDto()
    }

    protected fun generateResults(): AbstractResultDto
    {
        val resultDto = getResultDtoInstance()
        populateResultDto(resultDto, resultDto)

        return resultDto;
    }

    protected fun populateResultDto(resultDto: AbstractResultDto, result: Any)
    {
        if (errors.isNotEmpty()) {
            resultDto.setFail()
        } else {
            resultDto.setSuccess()
        }
    }
}