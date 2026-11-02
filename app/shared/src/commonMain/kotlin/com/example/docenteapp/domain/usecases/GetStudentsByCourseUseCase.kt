package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.StudentRepository
import com.example.docenteapp.domain.models.Student
import kotlinx.coroutines.flow.Flow

class GetStudentsByCourseUseCase(private val repository: StudentRepository) {
    operator fun invoke(courseId: String): Flow<List<Student>> {
        return repository.getStudentsByCourse(courseId)
    }
}
