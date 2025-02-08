package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.Test
import rs.playgroundmath.playgroundmath.repository.TestRepository

@Service
class TestServiceImpl(
    private val testRepository: TestRepository
): TestService {

    override fun saveTest(test: Test): Test =
        testRepository.save(test)

    override fun countUnresolvedTestsByAccountId(accountId: Long): Long =
        testRepository.countByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)

    override fun findUnresolvedTestsByAccountId(accountId: Long): Test =
        testRepository.findByAccount_AccountIdAndIsCompleted(accountId, YesNo.NO)
}