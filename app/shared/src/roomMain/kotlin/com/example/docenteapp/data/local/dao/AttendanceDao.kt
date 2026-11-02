package com.example.docenteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.docenteapp.data.local.entities.AttendanceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendance WHERE studentId = :studentId")
    fun getAttendanceForStudent(studentId: String): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE courseId = :courseId AND date = :date")
    fun getAttendanceForCourseAndDate(courseId: String, date: LocalDate): Flow<List<AttendanceEntity>>

    @Query("DELETE FROM attendance")
    suspend fun clearAll()
}
