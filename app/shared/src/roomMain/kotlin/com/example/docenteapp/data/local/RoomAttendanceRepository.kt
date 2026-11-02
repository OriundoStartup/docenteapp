package com.example.docenteapp.data.local

import com.example.docenteapp.data.local.dao.AttendanceDao
import com.example.docenteapp.data.local.mappers.toDomain
import com.example.docenteapp.data.local.mappers.toEntity
import com.example.docenteapp.domain.AttendanceRepository
import com.example.docenteapp.domain.models.Attendance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class RoomAttendanceRepository(private val attendanceDao: AttendanceDao) : AttendanceRepository {
    override suspend fun saveAttendance(attendance: List<Attendance>) {
        attendance.forEach {
            attendanceDao.insertAttendance(it.toEntity())
        }
    }

    override fun getAttendanceForStudent(studentId: String): Flow<List<Attendance>> {
        return attendanceDao.getAttendanceForStudent(studentId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAttendanceForCourseAndDate(courseId: String, date: LocalDate): Flow<List<Attendance>> {
        return attendanceDao.getAttendanceForCourseAndDate(courseId, date).map { list ->
            list.map { it.toDomain() }
        }
    }
}
