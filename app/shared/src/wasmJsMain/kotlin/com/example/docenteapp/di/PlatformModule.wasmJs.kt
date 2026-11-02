package com.example.docenteapp.di

import com.example.docenteapp.domain.*
import com.example.docenteapp.domain.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.module.Module
import org.koin.dsl.module

private class WasmMockUserRepository : UserRepository {
    override fun saveSession(user: User) {}
    override fun getSession(): User? = null
    override fun clearSession() {}
    override suspend fun saveUser(user: User, passwordHash: String) {}
    override suspend fun getUserByEmail(email: String): User? = null
    override suspend fun getPasswordHash(email: String): String? = null
    override suspend fun loginRemote(email: String, password: String): User = throw Exception("Not implemented")
    override fun getAllUsersFlow(): Flow<List<User>> = emptyFlow()
    override suspend fun getUserById(id: String): User? = null
}

private class WasmMockCourseRepository : CourseRepository {
    override suspend fun saveCourse(course: Course) {}
    override suspend fun updateCourse(course: Course) {}
    override suspend fun deleteCourse(courseId: String) {}
    override suspend fun getCourseById(id: String): Course? = null
    override suspend fun getCourseByAccessCode(code: String): Course? = null
    override fun getAllCourses(): Flow<List<Course>> = emptyFlow()
    override fun getProfessorCourses(professorId: String): Flow<List<Course>> = emptyFlow()
}

private class WasmMockCommunicationRepository : CommunicationRepository {
    override fun getCommunicationsForUser(userId: String): Flow<List<Communication>> = emptyFlow()
    override suspend fun saveCommunication(communication: Communication) {}
    override suspend fun markAsRead(communicationId: String) {}
}

private class WasmMockAttendanceRepository : AttendanceRepository {
    override suspend fun saveAttendance(attendance: List<Attendance>) {}
    override fun getAttendanceForStudent(studentId: String): Flow<List<Attendance>> = emptyFlow()
}

private class WasmMockGradeRepository : GradeRepository {
    override suspend fun saveGrades(grades: List<Grade>) {}
    override fun getGradesForStudent(studentId: String): Flow<List<Grade>> = emptyFlow()
}

private class WasmMockStudentRepository : StudentRepository {
    override suspend fun saveStudent(student: Student) {}
    override suspend fun getStudentById(id: String): Student? = null
    override fun getParentStudents(parentId: String): Flow<List<Student>> = emptyFlow()
    override fun getStudentsByCourse(courseId: String): Flow<List<Student>> = emptyFlow()
}

private class WasmMockCollegeRepository : CollegeRepository {
    override suspend fun saveCollege(college: College) {}
    override suspend fun getCollegeById(id: String): College? = null
    override suspend fun getCollegeByName(name: String): College? = null
}

actual val platformModule: Module = module {
    single<UserRepository> { WasmMockUserRepository() }
    single<CollegeRepository> { WasmMockCollegeRepository() }
    single<CourseRepository> { WasmMockCourseRepository() }
    single<CommunicationRepository> { WasmMockCommunicationRepository() }
    single<AttendanceRepository> { WasmMockAttendanceRepository() }
    single<GradeRepository> { WasmMockGradeRepository() }
    single<StudentRepository> { WasmMockStudentRepository() }
}
