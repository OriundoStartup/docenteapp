package com.example.docenteapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "colleges",
    indices = [Index(value = ["identifier"], unique = true)]
)
data class CollegeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val identifier: String
)
