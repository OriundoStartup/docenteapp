package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docenteapp.domain.AttendanceRepository
import com.example.docenteapp.domain.models.*
import com.example.docenteapp.domain.usecases.GenerateCertificateUseCase
import com.example.docenteapp.domain.usecases.GetStudentDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudentDetailUiState(
    val selectedStudent: Student? = null,
    val attendanceHistory: List<Attendance> = emptyList(),
    val isLoading: Boolean = false,
    val generatedCertificate: Certificate? = null,
)

class StudentDetailViewModel(
    private val getStudentDetailUseCase: GetStudentDetailUseCase,
    private val generateCertificateUseCase: GenerateCertificateUseCase,
    private val attendanceRepository: AttendanceRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(StudentDetailUiState())
    val state: StateFlow<StudentDetailUiState> = _state.asStateFlow()

    fun loadStudentDetail(studentId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val student = getStudentDetailUseCase(studentId)
            
            attendanceRepository.getAttendanceForStudent(studentId).collect { history ->
                _state.update { it.copy(
                    selectedStudent = student,
                    attendanceHistory = history,
                    isLoading = false
                ) }
            }
        }
    }

    fun generateReport(type: CertificateType, onSuccess: () -> Unit) {
        val student = _state.value.selectedStudent ?: return
        viewModelScope.launch {
            val certificate = generateCertificateUseCase(student, type)
            _state.update { it.copy(generatedCertificate = certificate) }
            onSuccess()
        }
    }
}
