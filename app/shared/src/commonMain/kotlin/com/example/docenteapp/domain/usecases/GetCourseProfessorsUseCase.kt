package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetCourseProfessorsUseCase(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(courseId: String): List<User> {
        val course = courseRepository.getCourseById(courseId) ?: return emptyList()
        val professor = userRepository.getUserById(course.professorId)
        return if (professor != null) listOf(professor) else emptyList()
    }
}
