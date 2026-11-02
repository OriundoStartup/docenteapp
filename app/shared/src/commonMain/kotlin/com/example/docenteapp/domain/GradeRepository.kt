package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.Grade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    suspend fun saveGrades(grades: List<Grade>)
    fun getGradesForStudent(studentId: String): Flow<List<Grade>>
}
