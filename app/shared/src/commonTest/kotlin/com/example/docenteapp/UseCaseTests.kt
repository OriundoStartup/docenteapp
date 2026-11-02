package com.example.docenteapp

import com.example.docenteapp.data.PasswordHasher
import com.example.docenteapp.domain.AttendanceRepository
import com.example.docenteapp.domain.CollegeRepository
import com.example.docenteapp.domain.CommunicationRepository
import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.EvaluationRepository
import com.example.docenteapp.domain.GradeRepository
import com.example.docenteapp.domain.StudentRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.Attendance
import com.example.docenteapp.domain.models.AttendanceStatus
import com.example.docenteapp.domain.models.College
import com.example.docenteapp.domain.models.Communication
import com.example.docenteapp.domain.models.Course
import com.example.docenteapp.domain.models.Evaluation
import com.example.docenteapp.domain.models.Grade
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.domain.models.User
import com.example.docenteapp.domain.models.UserRole
import com.example.docenteapp.domain.usecases.CreateCourseUseCase
import com.example.docenteapp.domain.usecases.DeleteCourseUseCase
import com.example.docenteapp.domain.usecases.LinkStudentUseCase
import com.example.docenteapp.domain.usecases.LoginUseCase
import com.example.docenteapp.domain.usecases.RegisterUseCase
import com.example.docenteapp.domain.usecases.SaveAttendanceUseCase
import com.example.docenteapp.domain.usecases.SendCommunicationUseCase
import com.example.docenteapp.domain.usecases.UpdateCourseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// --- Mocks y Fakes para Tests Unitarios con firmas 3NF ---

class FakeUserRepository : UserRepository {
    private var currentSession: User? = null
    private val usersMap = mutableMapOf<String, User>()
    private val passwordsMap = mutableMapOf<String, String>()

    override fun saveSession(user: User) {
        currentSession = user
    }

    override fun getSession(): User? = currentSession

    override fun clearSession() {
        currentSession = null
    }

    override suspend fun saveUser(user: User, passwordHash: String) {
        usersMap[user.email] = user
        passwordsMap[user.email] = passwordHash
    }

    override suspend fun getUserByEmail(email: String): User? = usersMap[email]

    override suspend fun getPasswordHash(email: String): String? = passwordsMap[email]

    override suspend fun loginRemote(email: String, password: String): User {
        val user = usersMap[email] ?: throw Exception("Usuario no encontrado")
        val storedHash = passwordsMap[email]
        if (storedHash != PasswordHasher.hash(password)) {
            throw Exception("Contraseña incorrecta")
        }
        return user
    }

    override fun getAllUsersFlow(): Flow<List<User>> = flowOf(usersMap.values.toList())

    override suspend fun getUserById(id: String): User? = usersMap.values.find { it.id == id }
}

class FakeCourseRepository : CourseRepository {
    private val courses = mutableListOf<Course>()

    override suspend fun saveCourse(course: Course) {
        courses.removeAll { it.id == course.id }
        courses.add(course)
    }

    override suspend fun updateCourse(course: Course) {
        val index = courses.indexOfFirst { it.id == course.id }
        if (index != -1) {
            courses[index] = course
        } else {
            courses.add(course)
        }
    }

    override suspend fun deleteCourse(courseId: String) {
        courses.removeAll { it.id == courseId }
    }

    override suspend fun getCourseById(id: String): Course? = courses.find { it.id == id }

    override suspend fun getCourseByAccessCode(code: String): Course? =
        courses.find { it.accessCode.equals(code, ignoreCase = true) }

    override fun getAllCourses(): Flow<List<Course>> = flowOf(courses.toList())

    override fun getProfessorCourses(professorId: String): Flow<List<Course>> =
        flowOf(courses.filter { it.professorId == professorId })
}

class FakeCollegeRepository : CollegeRepository {
    private val colleges = mutableListOf<College>()

    override suspend fun saveCollege(college: College) {
        colleges.removeAll { it.id == college.id }
        colleges.add(college)
    }

    override suspend fun getCollegeById(id: String): College? = colleges.find { it.id == id }

    override suspend fun getCollegeByName(name: String): College? =
        colleges.find { it.name.equals(name, ignoreCase = true) }
}

class FakeStudentRepository : StudentRepository {
    private val students = mutableListOf<Student>()

    override suspend fun saveStudent(student: Student) {
        students.removeAll { it.id == student.id }
        students.add(student)
    }

    override suspend fun getStudentById(id: String): Student? = students.find { it.id == id }

    override fun getParentStudents(parentId: String): Flow<List<Student>> =
        flowOf(students.filter { it.parentId == parentId })

    override fun getStudentsByCourse(courseId: String): Flow<List<Student>> =
        flowOf(students.filter { it.courseId == courseId })
}

class FakeCommunicationRepository : CommunicationRepository {
    private val communications = mutableListOf<Communication>()

    override fun getCommunicationsForUser(userId: String): Flow<List<Communication>> =
        flowOf(communications.filter { (it.receiverId == userId) || (it.senderId == userId) })

    override suspend fun saveCommunication(communication: Communication) {
        communications.add(communication)
    }

    override suspend fun markAsRead(communicationId: String) {
        val index = communications.indexOfFirst { it.id == communicationId }
        if (index != -1) {
            communications[index] = communications[index].copy(isRead = true)
        }
    }
}

class FakeAttendanceRepository : AttendanceRepository {
    private val attendanceRecords = mutableListOf<Attendance>()

    override suspend fun saveAttendance(attendance: List<Attendance>) {
        attendanceRecords.addAll(attendance)
    }

    override fun getAttendanceForStudent(studentId: String): Flow<List<Attendance>> =
        flowOf(attendanceRecords.filter { it.studentId == studentId })

    override fun getAttendanceForCourseAndDate(courseId: String, date: LocalDate): Flow<List<Attendance>> =
        flowOf(attendanceRecords.filter { it.courseId == courseId && it.date == date })
}

class FakeGradeRepository : GradeRepository {
    private val gradeRecords = mutableListOf<Grade>()

    override suspend fun saveGrades(grades: List<Grade>) {
        gradeRecords.addAll(grades)
    }

    override fun getGradesForStudent(studentId: String): Flow<List<Grade>> =
        flowOf(gradeRecords.filter { it.studentId == studentId })
}

class FakeEvaluationRepository : EvaluationRepository {
    private val evaluations = mutableListOf<Evaluation>()

    override fun saveEvaluation(evaluation: Evaluation) {
        evaluations.removeAll { it.id == evaluation.id }
        evaluations.add(evaluation)
    }

    override fun getEvaluationById(id: String): Evaluation? = evaluations.find { it.id == id }
}

// --- Suite de Pruebas Normalizada ---

class UseCaseTests {

    @Test
    fun testLoginValidationAndFallback() = runTest {
        val userRepo = FakeUserRepository()
        val loginUseCase = LoginUseCase(userRepo)
        val registerUseCase = RegisterUseCase(userRepo)

        // Email / password vacíos fallan
        assertTrue(loginUseCase("", "pass").isFailure)
        assertTrue(loginUseCase("user@test.com", "").isFailure)

        // Registrar usuario
        val regResult = registerUseCase("Carlos", "Gomez", "carlos@test.com", "secret123", UserRole.PROFESSOR)
        assertTrue(regResult.isSuccess)

        // Login remoto o local exitoso
        val loginResult = loginUseCase("carlos@test.com", "secret123")
        assertTrue(loginResult.isSuccess)
        assertEquals("carlos@test.com", loginResult.getOrNull()?.email)
    }

    @Test
    fun testRegisterUserValidations() = runTest {
        val userRepo = FakeUserRepository()
        val registerUseCase = RegisterUseCase(userRepo)

        // Contraseña muy corta
        val shortPass = registerUseCase("Ana", "Silva", "ana@test.com", "123", UserRole.PARENT)
        assertTrue(shortPass.isFailure)

        // Email inválido
        val invalidEmail = registerUseCase("Ana", "Silva", "ana_sin_arroba", "123456", UserRole.PARENT)
        assertTrue(invalidEmail.isFailure)

        // Registro exitoso
        val success = registerUseCase("Ana", "Silva", "ana@test.com", "123456", UserRole.PARENT)
        assertTrue(success.isSuccess)

        // Email duplicado
        val duplicate = registerUseCase("Ana", "Silva", "ana@test.com", "123456", UserRole.PARENT)
        assertTrue(duplicate.isFailure)
    }

    @Test
    fun testCreateCourseSuccessfully() = runTest {
        val userRepo = FakeUserRepository()
        val courseRepo = FakeCourseRepository()
        val collegeRepo = FakeCollegeRepository()

        val testUser = User(
            id = "prof_1",
            firstName = "Juan",
            lastName = "Perez",
            email = "juan@colegio.cl",
            role = UserRole.PROFESSOR
        )
        userRepo.saveSession(testUser)

        val createCourseUseCase = CreateCourseUseCase(courseRepo, userRepo, collegeRepo)
        val result = createCourseUseCase("Matemáticas 1º Medio", "Colegio Central", "2026")

        assertTrue(result.isSuccess)
        val course = result.getOrNull()
        assertNotNull(course)
        assertEquals("Matemáticas 1º Medio", course.name)
        assertEquals("prof_1", course.professorId)

        // Verificar persistencia del colegio
        val savedCollege = collegeRepo.getCollegeByName("Colegio Central")
        assertNotNull(savedCollege)
    }

    @Test
    fun testUpdateAndDeleteCourse() = runTest {
        val courseRepo = FakeCourseRepository()
        val updateCourseUseCase = UpdateCourseUseCase(courseRepo)
        val deleteCourseUseCase = DeleteCourseUseCase(courseRepo)

        val course = Course("course_1", "Historia", "prof_1", "college_1", "HIST12", "2026")
        courseRepo.saveCourse(course)

        // Actualizar
        val updatedCourse = course.copy(name = "Historia Universal")
        val updateResult = updateCourseUseCase(updatedCourse)
        assertTrue(updateResult.isSuccess)
        assertEquals("Historia Universal", courseRepo.getCourseById("course_1")?.name)

        // Eliminar
        val deleteResult = deleteCourseUseCase("course_1")
        assertTrue(deleteResult.isSuccess)
        assertEquals(null, courseRepo.getCourseById("course_1"))
    }

    @Test
    fun testSaveAttendanceUseCase() = runTest {
        val attendanceRepo = FakeAttendanceRepository()
        val saveAttendanceUseCase = SaveAttendanceUseCase(attendanceRepo)

        val attendanceMap = mapOf(
            "student_1" to AttendanceStatus.PRESENT,
            "student_2" to AttendanceStatus.ABSENT
        )

        val result = saveAttendanceUseCase("course_1", LocalDate(2026, 3, 10), attendanceMap)
        assertTrue(result.isSuccess)

        val student1Attendance = attendanceRepo.getAttendanceForStudent("student_1").first()
        assertEquals(1, student1Attendance.size)
        assertEquals(AttendanceStatus.PRESENT, student1Attendance.first().status)
    }

    @Test
    fun testSendCommunicationUseCase() = runTest {
        val commRepo = FakeCommunicationRepository()
        val userRepo = FakeUserRepository()
        val sendCommUseCase = SendCommunicationUseCase(commRepo, userRepo)

        val sender = User("prof_1", "Maria", "Lopez", "maria@test.com", UserRole.PROFESSOR)
        userRepo.saveSession(sender)

        // Intento con título vacío
        val failResult = sendCommUseCase("", "Contenido", "parent_1")
        assertTrue(failResult.isFailure)

        // Envío exitoso
        val successResult = sendCommUseCase("Reunión", "Estimado apoderado...", "parent_1")
        assertTrue(successResult.isSuccess)

        val comms = commRepo.getCommunicationsForUser("parent_1").first()
        assertEquals(1, comms.size)
        assertEquals("Reunión", comms.first().title)
    }

    @Test
    fun testLinkStudentUseCase() = runTest {
        val courseRepo = FakeCourseRepository()
        val studentRepo = FakeStudentRepository()
        val userRepo = FakeUserRepository()
        val linkStudentUseCase = LinkStudentUseCase(courseRepo, studentRepo, userRepo)

        val parent = User("parent_1", "Pedro", "Soto", "pedro@test.com", UserRole.PARENT)
        userRepo.saveSession(parent)

        val course = Course("course_1", "Ciencias 2º Medio", "prof_1", "college_1", "CIEN2M", "2026")
        courseRepo.saveCourse(course)

        // Código corto debe fallar
        val shortCode = linkStudentUseCase("123", "Lucas", "Soto", "21.000.000-1")
        assertTrue(shortCode.isFailure)

        // Vinculación exitosa con código correcto
        val result = linkStudentUseCase("CIEN2M", "Lucas", "Soto", "21.000.000-1")
        assertTrue(result.isSuccess)

        val parentStudents = studentRepo.getParentStudents("parent_1").first()
        assertEquals(1, parentStudents.size)
        assertEquals("Lucas", parentStudents.first().firstName)
    }

    @Test
    fun testGradeRepositorySaveAndQuery() = runTest {
        val gradeRepo = FakeGradeRepository()
        val grades = listOf(
            Grade("grade_1", "student_1", "eval_1", 6.5f),
            Grade("grade_2", "student_1", "eval_2", 7.0f)
        )

        gradeRepo.saveGrades(grades)
        val studentGrades = gradeRepo.getGradesForStudent("student_1").first()

        assertEquals(2, studentGrades.size)
        assertEquals(6.5f, studentGrades[0].value)
        assertEquals(7.0f, studentGrades[1].value)
    }

    @Test
    fun testEvaluationRepositorySaveAndQuery() = runTest {
        val evalRepo = FakeEvaluationRepository()
        val evaluation = Evaluation(
            id = "eval_1",
            courseId = "course_1",
            description = "Prueba de Historia",
            date = LocalDate(2026, 4, 15)
        )

        evalRepo.saveEvaluation(evaluation)
        val fetched = evalRepo.getEvaluationById("eval_1")

        assertNotNull(fetched)
        assertEquals("Prueba de Historia", fetched.description)
        assertEquals("course_1", fetched.courseId)
    }
}
