package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.docenteapp.domain.models.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

data class GradesUiState(
    val selectedCourseId: String = "",
    val selectedCourseName: String = "",
    val evaluationDescription: String = "",
    val students: List<Student> = emptyList(),
    val gradesMap: Map<String, String> = emptyMap(),
    val evaluationDate: LocalDate = LocalDate(2025, 1, 1)
)

class GradesViewModel : ViewModel() {
    private val _state = MutableStateFlow(GradesUiState())
    val state: StateFlow<GradesUiState> = _state.asStateFlow()

    fun onDescriptionChange(value: String) = _state.update { it.copy(evaluationDescription = value) }
    fun onGradeChange(studentId: String, value: String) {
        val newMap = _state.value.gradesMap.toMutableMap()
        newMap[studentId] = value
        _state.update { it.copy(gradesMap = newMap) }
    }
}
