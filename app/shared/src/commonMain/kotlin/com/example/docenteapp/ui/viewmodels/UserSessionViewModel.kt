package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.docenteapp.domain.models.User
import com.example.docenteapp.domain.usecases.GetSessionUseCase
import com.example.docenteapp.domain.usecases.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserSessionUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = true
)

class UserSessionViewModel(
    private val getSessionUseCase: GetSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserSessionUiState())
    val state: StateFlow<UserSessionUiState> = _state.asStateFlow()

    init {
        loadSession()
    }

    fun loadSession() {
        val user = getSessionUseCase()
        _state.update { it.copy(currentUser = user, isLoading = false) }
    }

    fun logout(onSuccess: () -> Unit) {
        logoutUseCase()
        _state.update { UserSessionUiState(isLoading = false) }
        onSuccess()
    }

    fun updateSession(user: User) {
        _state.update { it.copy(currentUser = user) }
    }
}
