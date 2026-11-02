package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.StudentRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetParentStudentsUseCase(
    private val studentRepository: StudentRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<Student>> {
        val user = userRepository.getSession() ?: return emptyFlow()
        return studentRepository.getParentStudents(user.id)
    }
}
