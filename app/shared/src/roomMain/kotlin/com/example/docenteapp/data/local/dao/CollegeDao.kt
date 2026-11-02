package com.example.docenteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.docenteapp.data.local.entities.CollegeEntity

@Dao
interface CollegeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollege(college: CollegeEntity)

    @Query("SELECT * FROM colleges WHERE name = :name LIMIT 1")
    suspend fun getCollegeByName(name: String): CollegeEntity?

    @Query("SELECT * FROM colleges WHERE id = :id LIMIT 1")
    suspend fun getCollegeById(id: String): CollegeEntity?
}
