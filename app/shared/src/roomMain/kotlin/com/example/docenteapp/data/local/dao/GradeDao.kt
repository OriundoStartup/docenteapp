package com.example.docenteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.docenteapp.data.local.entities.GradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: GradeEntity)

    @Query("SELECT * FROM grades WHERE studentId = :studentId")
    fun getGradesForStudent(studentId: String): Flow<List<GradeEntity>>

    @Query("SELECT * FROM grades WHERE evaluationId = :evaluationId")
    fun getGradesForEvaluation(evaluationId: String): Flow<List<GradeEntity>>

    @Query("DELETE FROM grades")
    suspend fun clearAll()
}
