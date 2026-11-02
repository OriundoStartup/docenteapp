package com.example.docenteapp.data.local

import com.example.docenteapp.data.local.dao.UserDao
import com.example.docenteapp.data.local.mappers.toDomain
import com.example.docenteapp.data.local.mappers.toEntity
import com.example.docenteapp.data.remote.AuthApiService
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.User
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

private val json = Json { ignoreUnknownKeys = true }

class RoomUserRepository(
    private val userDao: UserDao,
    private val settings: Settings,
    private val authApiService: AuthApiService
) : UserRepository {
    private val sessionKey = "user_session"
    
    // In-memory cache
    private var cachedUser: User? = null

    override fun saveSession(user: User) {
        try {
            cachedUser = user
            val sessionJson = json.encodeToString(user)
            settings.putString(sessionKey, sessionJson)
        } catch (_: Exception) {
            // Handle error silently
        }
    }

    override fun getSession(): User? {
        if (cachedUser != null) {
            return cachedUser
        }
        
        return try {
            val sessionJson = settings.getStringOrNull(sessionKey)
            if (sessionJson == null) {
                null
            } else {
                val user = json.decodeFromString<User>(sessionJson)
                cachedUser = user
                user
            }
        } catch (_: Exception) {
            null
        }
    }

    override fun clearSession() {
        cachedUser = null
        settings.remove(sessionKey)
    }

    override suspend fun saveUser(user: User, passwordHash: String) {
        val entity = user.toEntity().copy(passwordHash = passwordHash)
        userDao.insertUser(entity)
    }

    override suspend fun getUserByEmail(email: String): User? {
        val local = userDao.getUserByEmail(email.trim().lowercase())
        return local?.toDomain()
    }

    override suspend fun getPasswordHash(email: String): String? {
        return userDao.getUserByEmail(email.trim().lowercase())?.passwordHash
    }

    override suspend fun loginRemote(email: String, password: String): User {
        val user = authApiService.login(email, password)
        // Ensure user is in local DB
        userDao.insertUser(user.toEntity())
        saveSession(user)
        return user
    }

    override fun getAllUsersFlow(): Flow<List<User>> {
        return userDao.getAllUsers().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getUserById(id: String): User? {
        return userDao.getUserById(id)?.toDomain()
    }
}
