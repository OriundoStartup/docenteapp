package com.example.docenteapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["professorId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CollegeEntity::class,
            parentColumns = ["id"],
            childColumns = ["collegeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["professorId"]),
        Index(value = ["collegeId"])
    ]
)
data class CourseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val professorId: String?,
    val collegeId: String,
    val accessCode: String,
    val period: String
)
