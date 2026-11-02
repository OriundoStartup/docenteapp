package com.example.docenteapp.domain.usecases

import com.example.docenteapp.data.PasswordHasher
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.User

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isEmpty() || !email.contains("@")) return Result.failure(Exception("Email inválido"))
        if (password.isEmpty()) return Result.failure(Exception("Contraseña requerida"))

        return try {
            // Primero intentamos login remoto para asegurar sincronización y credenciales frescas
            val user = repository.loginRemote(email, password)
            Result.success(user)
        } catch (remoteError: Exception) {
            // Si el remoto falla (ej: sin internet), intentamos login local con datos guardados
            val user = repository.getUserByEmail(email)
            if (user != null) {
                val storedHash = repository.getPasswordHash(email)
                val currentHash = PasswordHasher.hash(password)
                
                if (storedHash == currentHash) {
                    repository.saveSession(user)
                    Result.success(user)
                } else {
                    Result.failure(Exception("Contraseña incorrecta (Local)"))
                }
            } else {
                Result.failure(Exception("Error de conexión y usuario no encontrado localmente"))
            }
        }
    }
}
