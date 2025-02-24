package rs.playgroundmath.playgroundmath

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.springframework.boot.test.context.SpringBootTest
import rs.playgroundmath.playgroundmath.service.TaskServiceImpl
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertTrue

@SpringBootTest
class TaskServiceImplTest {

    private val taskService = TaskServiceImpl(mockk(), mockk(), mockk(), mockk(), mockk())


    @RepeatedTest(100)
    fun `should generate valid second number for sum`() {
        val method = TaskServiceImpl::class.declaredFunctions
            .first { it.name == "generateSecondNumberForSum" }
            .apply { isAccessible = true }

        val firstNumber = 14
        val numberTwoFrom = 1
        val numberTwoTo = 20

        val result = method.call(taskService, firstNumber, numberTwoFrom, numberTwoTo, false, false) as Long

        assertTrue(((firstNumber % 10) + (result % 10)) < 10, "Zbir jedinica ne sme prelaziti u sledecu deseticu")

        assertTrue((firstNumber + result) < 100, "Zbir ne sme prelaziti 99")
    }

    @RepeatedTest(100)
    fun `should generate valid second number for sub`() {
        val method = TaskServiceImpl::class.declaredFunctions
            .first { it.name == "generateSecondNumberForSub" }
            .apply { isAccessible = true }

        val firstNumber = 14
        val numberTwoFrom = 1
        val numberTwoTo = 20

        val result = method.call(taskService, firstNumber, numberTwoFrom, numberTwoTo, false, false) as Long

        assertTrue(firstNumber - result > 0, "Razlika ne sme biti negativna")

        assertTrue((result % 10) <= (firstNumber % 10), "Jedinica drugog broje ne sme biti veca od jedinice prvog broja")
    }

    @RepeatedTest(100)
    fun `should generate valid second number for mul`() {
        val method = TaskServiceImpl::class.declaredFunctions
            .first { it.name == "generateSecondNumberForMul" }
            .apply { isAccessible = true }

        val firstNumber = 14
        val numberTwoFrom = 1
        val numberTwoTo = 20

        val result = method.call(taskService, firstNumber, numberTwoFrom, numberTwoTo, false) as Long

        assertTrue((firstNumber * result) < 100, "Proizvod ne sme prelaziti 99")
    }

    @RepeatedTest(100)
    fun `should generate valid second number for div`() {
        val method = TaskServiceImpl::class.declaredFunctions
            .first { it.name == "generateSecondNumberForDiv" }
            .apply { isAccessible = true }

        val firstNumber = 24L
        val numberTwoFrom = 1L
        val numberTwoTo = 12L

        val result = method.call(taskService, firstNumber, numberTwoFrom, numberTwoTo) as Long

        assertEquals(0L, firstNumber % result, "Prvi broj ($firstNumber) nije deljiv sa generisanim brojem ($result)")
    }
}
