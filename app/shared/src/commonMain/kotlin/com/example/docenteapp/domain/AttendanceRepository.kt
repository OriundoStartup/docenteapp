package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.Attendance
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface AttendanceRepository {
    suspend fun saveAttendance(attendance: List<Attendance>)
    fun getAttendanceForStudent(studentId: String): Flow<List<Attendance>>
    fun getAttendanceForCourseAndDate(courseId: String, date: LocalDate): Flow<List<Attendance>>
}
