package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.StudentRepository
import com.example.docenteapp.domain.models.Student

class GetStudentDetailUseCase(
    private val studentRepository: StudentRepository
) {
    suspend operator fun invoke(studentId: String): Student? {
        return studentRepository.getStudentById(studentId)
    }
}
