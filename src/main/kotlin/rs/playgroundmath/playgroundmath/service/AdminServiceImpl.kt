package rs.playgroundmath.playgroundmath.service

import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.enums.RoleType
import rs.playgroundmath.playgroundmath.payload.response.AdminCourseResponse
import rs.playgroundmath.playgroundmath.payload.response.AdminTaskResponse
import rs.playgroundmath.playgroundmath.payload.response.AdminTeacherResponse
import rs.playgroundmath.playgroundmath.payload.response.AdminTestResponse
import rs.playgroundmath.playgroundmath.repository.CourseRepository
import rs.playgroundmath.playgroundmath.repository.RoleRepository
import rs.playgroundmath.playgroundmath.repository.TestRepository
import rs.playgroundmath.playgroundmath.repository.UserRepository

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val courseRepository: CourseRepository,
    private val testRepository: TestRepository
) : AdminService {

    override fun getTeacherInformation(): List<AdminTeacherResponse> {
        val role = roleRepository.findRoleByRoleType(RoleType.TEACHER)

        val users = userRepository.findAllByRole(role!!)

        return users.map { user ->

            val coursesResponse = user.courses.map { course ->

                val testResponse = course.tests.map { test ->

                    val tasksResponse = test.tasks?.map { task ->
                        AdminTaskResponse(
                            taskId = task.taskId,
                            task = "${task.firstNumber} ${task.operation} ${task.secondNumber} = ${task.result}"
                        )
                    }

                    AdminTestResponse(
                        testId = test.testId,
                        tasks = tasksResponse
                    )
                }

                AdminCourseResponse(
                    courseId = course.courseId,
                    courseName = course.title,
                    courseDescription = course.description,
                    courseAge = course.age,
                    tests = testResponse
                )
            }

            AdminTeacherResponse(
                teacherId = user.userId,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                courses = coursesResponse
            )
        }
    }

    override fun deleteCourseByAdmin(courseId: Long): Boolean {
        val course = courseRepository.findByCourseId(courseId)

        return if (course == null) {
            false
        } else {
            courseRepository.delete(course)
            true
        }
    }

    override fun deleteTestByAdmin(testId: Long): Boolean {
        val test = testRepository.findByTestId(testId)

        return if (test == null) {
            false
        } else {
            testRepository.delete(test)
            true
        }
    }
}