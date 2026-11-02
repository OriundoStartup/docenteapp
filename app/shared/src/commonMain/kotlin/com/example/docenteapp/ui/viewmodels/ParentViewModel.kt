package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docenteapp.domain.models.RecentActivity
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.domain.usecases.GetParentStudentsUseCase
import kotlinx.coroutines.flow.*

data class ParentUiState(
    val students: List<Student> = emptyList(),
    val recentActivities: List<RecentActivity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ParentViewModel(
    private val getParentStudentsUseCase: GetParentStudentsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParentUiState())
    val state: StateFlow<ParentUiState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _state.update { it.copy(isLoading = true) }
        getParentStudentsUseCase()
            .onEach { students ->
                _state.update { it.copy(students = students, isLoading = false) }
            }
            .catch { e ->
                _state.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}
