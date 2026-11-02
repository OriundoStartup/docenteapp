package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CourseRepository

class DeleteCourseUseCase(private val repository: CourseRepository) {
    suspend operator fun invoke(courseId: String): Result<Unit> {
        return try {
            repository.deleteCourse(courseId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
