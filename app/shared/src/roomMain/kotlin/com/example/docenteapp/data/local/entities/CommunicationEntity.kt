@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "communications",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["receiverId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["senderId"]),
        Index(value = ["receiverId"]),
        Index(value = ["sentAt"])
    ]
)
data class CommunicationEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val title: String,
    val content: String,
    val isRead: Boolean,
    val sentAt: Instant
)
