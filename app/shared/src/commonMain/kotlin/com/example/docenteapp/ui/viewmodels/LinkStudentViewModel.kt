package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docenteapp.domain.usecases.LinkStudentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LinkStudentUiState(
    val accessCode: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val rut: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class LinkStudentViewModel(
    private val linkStudentUseCase: LinkStudentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LinkStudentUiState())
    val state: StateFlow<LinkStudentUiState> = _state.asStateFlow()

    fun onAccessCodeChange(value: String) = _state.update { it.copy(accessCode = value, errorMessage = null) }
    fun onFirstNameChange(value: String) = _state.update { it.copy(firstName = value, errorMessage = null) }
    fun onLastNameChange(value: String) = _state.update { it.copy(lastName = value, errorMessage = null) }
    fun onRutChange(value: String) = _state.update { it.copy(rut = value, errorMessage = null) }

    fun linkStudent(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = linkStudentUseCase(
                _state.value.accessCode,
                _state.value.firstName,
                _state.value.lastName,
                _state.value.rut
            )
            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
                onSuccess()
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }
}
