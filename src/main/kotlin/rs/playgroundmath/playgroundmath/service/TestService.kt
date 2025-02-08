package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.model.Test

interface TestService {

    fun saveTest(test: Test): Test

    fun countUnresolvedTestsByAccountId(accountId: Long): Long

    fun findUnresolvedTestsByAccountId(accountId: Long): Test
}