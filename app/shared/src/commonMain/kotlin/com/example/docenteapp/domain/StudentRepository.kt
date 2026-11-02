package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    suspend fun saveStudent(student: Student)
    suspend fun getStudentById(id: String): Student?
    fun getParentStudents(parentId: String): Flow<List<Student>>
    fun getStudentsByCourse(courseId: String): Flow<List<Student>>
}
