package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun saveSession(user: User)
    fun getSession(): User?
    fun clearSession()
    suspend fun saveUser(user: User, passwordHash: String)
    suspend fun getUserByEmail(email: String): User?
    suspend fun getPasswordHash(email: String): String?
    suspend fun loginRemote(email: String, password: String): User
    fun getAllUsersFlow(): Flow<List<User>>
    suspend fun getUserById(id: String): User?
}
