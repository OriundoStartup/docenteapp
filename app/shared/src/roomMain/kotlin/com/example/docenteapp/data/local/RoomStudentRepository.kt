package com.example.docenteapp.data.local

import com.example.docenteapp.data.local.dao.StudentDao
import com.example.docenteapp.data.local.mappers.toDomain
import com.example.docenteapp.data.local.mappers.toEntity
import com.example.docenteapp.domain.StudentRepository
import com.example.docenteapp.domain.models.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomStudentRepository(private val studentDao: StudentDao) : StudentRepository {
    override suspend fun saveStudent(student: Student) {
        studentDao.insertStudent(student.toEntity())
    }

    override suspend fun getStudentById(id: String): Student? {
        return studentDao.getStudentById(id)?.toDomain()
    }

    override fun getParentStudents(parentId: String): Flow<List<Student>> {
        return studentDao.getStudentsByParent(parentId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getStudentsByCourse(courseId: String): Flow<List<Student>> {
        return studentDao.getStudentsByCourse(courseId).map { list ->
            list.map { it.toDomain() }
        }
    }
}
