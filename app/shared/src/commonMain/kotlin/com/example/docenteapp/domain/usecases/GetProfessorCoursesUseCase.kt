package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetProfessorCoursesUseCase(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<Course>> {
        val user = userRepository.getSession() ?: return emptyFlow()
        return courseRepository.getProfessorCourses(user.id) // Corrected: use user.id
    }
}
