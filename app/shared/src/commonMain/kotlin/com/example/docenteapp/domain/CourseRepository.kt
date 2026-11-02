package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun saveCourse(course: Course)
    suspend fun updateCourse(course: Course)
    suspend fun deleteCourse(courseId: String)
    suspend fun getCourseById(id: String): Course?
    suspend fun getCourseByAccessCode(code: String): Course?
    fun getAllCourses(): Flow<List<Course>>
    fun getProfessorCourses(professorId: String): Flow<List<Course>>
}
