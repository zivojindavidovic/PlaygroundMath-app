package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rs.playgroundmath.playgroundmath.enums.YesNo
import rs.playgroundmath.playgroundmath.model.Test

@Repository
interface TestRepository: JpaRepository<Test, Long> {

    fun countByAccount_AccountIdAndIsCompleted(accountId: Long, isCompleted: YesNo): Long

    fun findByAccount_AccountIdAndIsCompleted(accountId: Long, isCompleted: YesNo): Test

    fun findAllByCourse_CourseId(courseId: Long): List<Test>

    fun findByTestId(testId: Long): Test

    fun countTestByCourse_CourseId(courseId: Long): Long
}