package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docenteapp.domain.models.UserRole
import com.example.docenteapp.domain.usecases.LoginUseCase
import com.example.docenteapp.domain.usecases.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val loginEmail: String = "",
    val loginPassword: String = "",
    val registerName: String = "",
    val registerEmail: String = "",
    val registerPassword: String = "",
    val registerRole: UserRole = UserRole.PROFESSOR,
    val registerSpecialty: String = "",
    val registerPhone: String = "",
    val errorMessage: String? = null
)

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun onLoginEmailChange(value: String) = _state.update { it.copy(loginEmail = value, errorMessage = null) }
    fun onLoginPasswordChange(value: String) = _state.update { it.copy(loginPassword = value, errorMessage = null) }
    
    fun onRegisterNameChange(value: String) = _state.update { it.copy(registerName = value, errorMessage = null) }
    fun onRegisterEmailChange(value: String) = _state.update { it.copy(registerEmail = value, errorMessage = null) }
    fun onRegisterPasswordChange(value: String) = _state.update { it.copy(registerPassword = value, errorMessage = null) }
    fun onRegisterRoleChange(role: UserRole) = _state.update { it.copy(registerRole = role) }
    fun onRegisterSpecialtyChange(value: String) = _state.update { it.copy(registerSpecialty = value) }
    fun onRegisterPhoneChange(value: String) = _state.update { it.copy(registerPhone = value) }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = loginUseCase(_state.value.loginEmail, _state.value.loginPassword)
            result.onSuccess {
                onSuccess()
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = registerUseCase(
                firstName = _state.value.registerName,
                lastName = "",
                email = _state.value.registerEmail,
                password = _state.value.registerPassword,
                role = _state.value.registerRole
            )
            result.onSuccess {
                onSuccess()
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = error.message) }
            }
        }
    }
}
