package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.payload.response.AdminTeacherResponse

interface AdminService {

    fun getTeacherInformation(): List<AdminTeacherResponse>

    fun deleteCourseByAdmin(courseId: Long): Boolean

    fun deleteTestByAdmin(testId: Long): Boolean
}