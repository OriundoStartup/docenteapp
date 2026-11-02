package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CollegeRepository
import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.College
import com.example.docenteapp.domain.models.Course
import kotlin.random.Random

class CreateCourseUseCase(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
    private val collegeRepository: CollegeRepository
) {
    suspend operator fun invoke(name: String, collegeName: String, period: String): Result<Course> {
        val user = userRepository.getSession() ?: return Result.failure(Exception("No hay sesión"))
        if (name.isEmpty() || collegeName.isEmpty()) return Result.failure(Exception("Campos incompletos"))

        // Integrity Fix: Ensure the user (professor) is actually in the Room DB
        // This handles cases where destructive migration wiped the DB but session remained.
        try {
            if (userRepository.getUserByEmail(user.email) == null) {
                userRepository.saveUser(user, "migrated_session")
            }
        } catch (e: Exception) {
            println("DEBUG: Error asegurando usuario en DB: ${e.message}")
        }

        // Ensure the college exists in the DB to satisfy 3NF Foreign Key
        val college = collegeRepository.getCollegeByName(collegeName) ?: run {
            val newCollege = College(
                id = "college_" + Random.nextInt(1000, 9999).toString(),
                name = collegeName,
                address = "Dirección pendiente",
                identifier = "ID-" + Random.nextInt(100, 999).toString()
            )
            collegeRepository.saveCollege(newCollege)
            newCollege
        }

        val course = Course(
            id = Random.nextInt(1000, 9999).toString(),
            name = name,
            professorId = user.id,
            collegeId = college.id,
            accessCode = (1..6).map { ('A'..'Z').random() }.joinToString(""),
            period = period
        )
        
        return try {
            courseRepository.saveCourse(course)
            Result.success(course)
        } catch (e: Exception) {
            Result.failure(Exception("Error al guardar curso: ${e.message}"))
        }
    }
}
