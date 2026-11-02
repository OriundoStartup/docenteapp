@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp.domain.usecases

import com.example.docenteapp.data.PasswordHasher
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.User
import com.example.docenteapp.domain.models.UserRole
import kotlin.random.Random
import kotlin.time.ExperimentalTime

class RegisterUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        role: UserRole,
    ): Result<User> {
        if (email.isEmpty() || !email.contains("@")) return Result.failure(Exception("Email inválido"))
        if (password.length < 6) return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        if (firstName.isEmpty()) return Result.failure(Exception("Nombre requerido"))

        if (repository.getUserByEmail(email) != null) {
            return Result.failure(Exception("El correo ya se encuentra registrado"))
        }

        val hashedPassword = PasswordHasher.hash(password)
        val user = User(
            id = "user_" + Random.nextInt(1000, 9999).toString(),
            firstName = firstName,
            lastName = lastName,
            email = email,
            role = role,
        )
        repository.saveUser(user, hashedPassword)
        repository.saveSession(user)
        return Result.success(user)
    }
}
