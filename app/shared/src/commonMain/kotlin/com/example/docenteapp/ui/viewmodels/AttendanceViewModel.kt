@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docenteapp.domain.AttendanceRepository
import com.example.docenteapp.domain.models.AttendanceStatus
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.domain.usecases.GetStudentsByCourseUseCase
import com.example.docenteapp.domain.usecases.SaveAttendanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class AttendanceUiState(
    val selectedCourseId: String = "",
    val selectedCourseName: String = "",
    val date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val students: List<Student> = emptyList(),
    val attendanceMap: Map<String, AttendanceStatus> = emptyMap(),
    val isLoading: Boolean = false,
    val isSavedSuccess: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

private data class AttendanceData(
    val students: List<Student>,
    val attendanceMap: Map<String, AttendanceStatus>
)

class AttendanceViewModel(
    private val getStudentsByCourseUseCase: GetStudentsByCourseUseCase,
    private val saveAttendanceUseCase: SaveAttendanceUseCase,
    private val attendanceRepository: AttendanceRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AttendanceUiState())
    val state: StateFlow<AttendanceUiState> = _state.asStateFlow()

    fun initialize(courseId: String, courseName: String) {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        _state.update { it.copy(
            selectedCourseId = courseId,
            selectedCourseName = courseName,
            date = today,
            isLoading = true
        ) }
        
        viewModelScope.launch {
            combine(
                getStudentsByCourseUseCase(courseId),
                attendanceRepository.getAttendanceForCourseAndDate(courseId, today)
            ) { students, existingAttendance ->
                val savedMap = existingAttendance.associate { it.studentId to it.status }
                val attendanceMap = students.associate { student ->
                    student.id to (savedMap[student.id] ?: AttendanceStatus.PRESENT)
                }
                AttendanceData(students, attendanceMap)
            }.collect { data ->
                _state.update { it.copy(
                    students = data.students,
                    attendanceMap = data.attendanceMap,
                    isLoading = false
                ) }
            }
        }
    }

    fun onStatusChange(studentId: String, status: AttendanceStatus) {
        val newMap = _state.value.attendanceMap.toMutableMap()
        newMap[studentId] = status
        _state.update { it.copy(attendanceMap = newMap) }
    }

    fun saveAttendance(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }
            
            val result = saveAttendanceUseCase(
                courseId = _state.value.selectedCourseId,
                date = _state.value.date,
                attendanceMap = _state.value.attendanceMap
            )
            
            result.onSuccess {
                _state.update { it.copy(isSaving = false, isSavedSuccess = true) }
                onSuccess()
            }.onFailure { error ->
                _state.update { it.copy(isSaving = false, errorMessage = error.message) }
            }
        }
    }
}
