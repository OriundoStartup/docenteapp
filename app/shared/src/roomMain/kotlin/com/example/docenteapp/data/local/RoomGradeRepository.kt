package com.example.docenteapp.data.local

import com.example.docenteapp.data.local.dao.GradeDao
import com.example.docenteapp.data.local.mappers.toDomain
import com.example.docenteapp.data.local.mappers.toEntity
import com.example.docenteapp.domain.GradeRepository
import com.example.docenteapp.domain.models.Grade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomGradeRepository(private val gradeDao: GradeDao) : GradeRepository {
    override suspend fun saveGrades(grades: List<Grade>) {
        grades.forEach {
            gradeDao.insertGrade(it.toEntity())
        }
    }

    override fun getGradesForStudent(studentId: String): Flow<List<Grade>> {
        return gradeDao.getGradesForStudent(studentId).map { list ->
            list.map { it.toDomain() }
        }
    }
}
