package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docenteapp.domain.models.Communication
import com.example.docenteapp.domain.models.Course
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.domain.models.User
import com.example.docenteapp.domain.models.UserRole
import com.example.docenteapp.domain.usecases.GetCommunicationsUseCase
import com.example.docenteapp.domain.usecases.GetCourseProfessorsUseCase
import com.example.docenteapp.domain.usecases.GetParentCoursesUseCase
import com.example.docenteapp.domain.usecases.GetProfessorCoursesUseCase
import com.example.docenteapp.domain.usecases.GetStudentsByCourseUseCase
import com.example.docenteapp.domain.usecases.SendCommunicationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CommunicationUiState(
    val communications: List<Communication> = emptyList(),
    val commTitle: String = "",
    val commContent: String = "",
    val commReceiverId: String = "",
    val availableCourses: List<Course> = emptyList(),
    val availableStudents: List<Student> = emptyList(),
    val availableProfessors: List<User> = emptyList(),
    val isLoading: Boolean = false
)

class CommunicationViewModel(
    private val getProfessorCoursesUseCase: GetProfessorCoursesUseCase,
    private val getParentCoursesUseCase: GetParentCoursesUseCase,
    private val getCourseProfessorsUseCase: GetCourseProfessorsUseCase,
    private val getStudentsByCourseUseCase: GetStudentsByCourseUseCase,
    private val getCommunicationsUseCase: GetCommunicationsUseCase,
    private val sendCommunicationUseCase: SendCommunicationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CommunicationUiState())
    val state: StateFlow<CommunicationUiState> = _state.asStateFlow()

    fun loadInbox() {
        getCommunicationsUseCase()
            .onEach { list ->
                _state.update { it.copy(communications = list) }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadFormContext(userRole: UserRole) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            if (userRole == UserRole.PROFESSOR) {
                // Combina los flujos de estudiantes reactivamente para evitar bloqueos
                getProfessorCoursesUseCase()
                    .flatMapLatest { courses ->
                        _state.update { it.copy(availableCourses = courses) }
                        if (courses.isEmpty()) return@flatMapLatest flowOf(emptyList())
                        
                        val flows = courses.map { getStudentsByCourseUseCase(it.id) }
                        combine(flows) { arrays -> arrays.flatMap { it } }
                    }
                    .onEach { allStudents ->
                        _state.update { it -> it.copy(availableStudents = allStudents.distinctBy { it.id }, isLoading = false) }
                    }
                    .launchIn(viewModelScope)
            } else {
                val parentCourses = getParentCoursesUseCase.invoke()
                val professors = mutableListOf<User>()
                for ((id) in parentCourses) {
                    professors.addAll(getCourseProfessorsUseCase(id))
                }
                _state.update { it ->
                    it.copy(
                    availableCourses = parentCourses,
                    availableProfessors = professors.distinctBy { it.id },
                    isLoading = false
                ) }
            }
        }
    }

    fun onCommTitleChange(value: String) = _state.update { it.copy(commTitle = value) }
    fun onCommContentChange(value: String) = _state.update { it.copy(commContent = value) }
    fun onCommReceiverChange(value: String) = _state.update { it.copy(commReceiverId = value) }

    fun sendCommunication(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = sendCommunicationUseCase(
                _state.value.commTitle,
                _state.value.commContent,
                _state.value.commReceiverId
            )
            result.onSuccess {
                onSuccess()
            }
        }
    }
}
