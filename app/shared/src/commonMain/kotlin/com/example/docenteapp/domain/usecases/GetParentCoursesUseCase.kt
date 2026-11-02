package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.StudentRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.Course
import kotlinx.coroutines.flow.firstOrNull

class GetParentCoursesUseCase(
    private val studentRepository: StudentRepository,
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) {
    suspend fun invoke(): List<Course> {
        val user = userRepository.getSession() ?: return emptyList()
        val students = studentRepository.getParentStudents(user.id).firstOrNull() ?: return emptyList()
        val courses = mutableListOf<Course>()
        for (student in students) {
            courseRepository.getCourseById(student.courseId)?.let { courses.add(it) }
        }
        return courses.distinctBy { it.id }
    }
}
