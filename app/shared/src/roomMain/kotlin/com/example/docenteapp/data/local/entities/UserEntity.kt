@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.docenteapp.domain.models.UserRole
import kotlin.time.Instant
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
    val createdAt: Instant,
    val passwordHash: String? = null
)
