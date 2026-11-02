package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.User

class GetSessionUseCase(private val repository: UserRepository) {
    operator fun invoke(): User? = repository.getSession()
}
