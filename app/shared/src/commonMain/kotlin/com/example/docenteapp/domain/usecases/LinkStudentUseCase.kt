package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.StudentRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.Student
import kotlin.random.Random

class LinkStudentUseCase(
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        accessCode: String,
        firstName: String,
        lastName: String,
        rut: String
    ): Result<Unit> {
        return try {
            val user = userRepository.getSession() ?: return Result.failure(Exception("No hay sesión activa"))
            
            if (accessCode.length != 6) return Result.failure(Exception("El código debe tener 6 caracteres"))
            if (firstName.isEmpty() || rut.isEmpty()) return Result.failure(Exception("Faltan datos del estudiante"))

            val course = courseRepository.getCourseByAccessCode(accessCode.uppercase())
                ?: return Result.failure(Exception("Código de curso no válido"))

            val studentIdNum = Random.nextLong(1000000000L, 9999999999L)
            val student = Student(
                id = "student_$studentIdNum",
                firstName = firstName,
                lastName = lastName,
                rut = rut,
                courseId = course.id,
                parentId = user.id
            )

            studentRepository.saveStudent(student)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
