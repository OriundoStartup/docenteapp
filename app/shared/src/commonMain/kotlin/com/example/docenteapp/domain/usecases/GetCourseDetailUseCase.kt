package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.models.Course

class GetCourseDetailUseCase(private val repository: CourseRepository) {
    suspend operator fun invoke(courseId: String): Course? {
        return repository.getCourseById(courseId)
    }
}
