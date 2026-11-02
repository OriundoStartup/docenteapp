package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.UserRepository

class LogoutUseCase(private val repository: UserRepository) {
    operator fun invoke() = repository.clearSession()
}
