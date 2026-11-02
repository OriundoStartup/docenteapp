package com.example.docenteapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "students",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["rut"], unique = true),
        Index(value = ["courseId"]),
        Index(value = ["parentId"])
    ]
)
data class StudentEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val rut: String,
    val courseId: String,
    val parentId: String?
)
