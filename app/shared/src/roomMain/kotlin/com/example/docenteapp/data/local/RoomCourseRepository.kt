package com.example.docenteapp.data.local

import com.example.docenteapp.data.local.dao.CourseDao
import com.example.docenteapp.data.local.mappers.toDomain
import com.example.docenteapp.data.local.mappers.toEntity
import com.example.docenteapp.domain.CourseRepository
import com.example.docenteapp.domain.models.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class RoomCourseRepository(
    private val courseDao: CourseDao
) : CourseRepository {
    override suspend fun saveCourse(course: Course) {
        courseDao.insertCourse(course.toEntity())
    }

    override suspend fun updateCourse(course: Course) {
        courseDao.updateCourse(course.toEntity())
    }

    override suspend fun deleteCourse(courseId: String) {
        courseDao.deleteCourseById(courseId)
    }

    override suspend fun getCourseById(id: String): Course? {
        return courseDao.getCourseById(id)?.toDomain()
    }

    override suspend fun getCourseByAccessCode(code: String): Course? {
        return courseDao.getCourseByAccessCode(code)?.toDomain()
    }

    override fun getAllCourses(): Flow<List<Course>> {
        return emptyFlow()
    }

    override fun getProfessorCourses(professorId: String): Flow<List<Course>> {
        return courseDao.getCoursesByProfessor(professorId).map { list ->
            list.map { it.toDomain() }
        }
    }
}
