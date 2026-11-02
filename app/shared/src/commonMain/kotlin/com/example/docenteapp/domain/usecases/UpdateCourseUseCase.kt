package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.models.Course

class UpdateCourseUseCase(private val repository: CourseRepository) {
    suspend operator fun invoke(course: Course): Result<Unit> {
        return try {
            if (course.name.isEmpty()) return Result.failure(Exception("El nombre del curso no puede estar vacío"))
            repository.updateCourse(course)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
