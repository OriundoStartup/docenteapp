package com.example.docenteapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.docenteapp.domain.models.AttendanceStatus
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["studentId"]),
        Index(value = ["courseId"]),
        Index(value = ["date"])
    ]
)
data class AttendanceEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val courseId: String,
    val date: LocalDate,
    val status: AttendanceStatus
)
