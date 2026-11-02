package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.AttendanceRepository
import com.example.docenteapp.domain.models.Attendance
import com.example.docenteapp.domain.models.AttendanceStatus
import kotlinx.datetime.LocalDate
import kotlin.random.Random

class SaveAttendanceUseCase(private val repository: AttendanceRepository) {
    suspend operator fun invoke(
        courseId: String,
        date: LocalDate,
        attendanceMap: Map<String, AttendanceStatus>
    ): Result<Unit> {
        return try {
            val list = attendanceMap.map { (studentId, status) ->
                Attendance(
                    id = "att_" + Random.nextInt(1000, 9999).toString(),
                    studentId = studentId,
                    courseId = courseId,
                    date = date,
                    status = status
                )
            }
            repository.saveAttendance(list)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
