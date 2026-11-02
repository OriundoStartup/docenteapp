package com.example.docenteapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docenteapp.domain.models.Course
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.domain.usecases.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CourseUiState(
    val courses: List<Course> = emptyList(),
    val courseStudentsMap: Map<String, List<Student>> = emptyMap(),
    val selectedCourseStudents: List<Student> = emptyList(),
    val createCourseName: String = "",
    val createCollegeName: String = "",
    val createPeriod: String = "",
    val generatedCourseCode: String = "",
    val errorMessage: String? = null,
    val selectedCourse: Course? = null,
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val editName: String = "",
    val editPeriod: String = ""
)

class CourseViewModel(
    private val createCourseUseCase: CreateCourseUseCase,
    private val getProfessorCoursesUseCase: GetProfessorCoursesUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
    private val getStudentsByCourseUseCase: GetStudentsByCourseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CourseUiState())
    val state: StateFlow<CourseUiState> = _state.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        getProfessorCoursesUseCase()
            .onEach { list ->
                _state.update { it.copy(courses = list) }
                list.forEach { course ->
                    viewModelScope.launch {
                        getStudentsByCourseUseCase(course.id).collect { students ->
                            _state.update { current ->
                                val updatedMap = current.courseStudentsMap.toMutableMap()
                                updatedMap[course.id] = students
                                current.copy(courseStudentsMap = updatedMap)
                            }
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun onCourseNameChange(value: String) = _state.update { it.copy(createCourseName = value, errorMessage = null) }
    fun onCollegeNameChange(value: String) = _state.update { it.copy(createCollegeName = value, errorMessage = null) }
    fun onPeriodChange(value: String) = _state.update { it.copy(createPeriod = value, errorMessage = null) }

    fun createCourse(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = createCourseUseCase(
                _state.value.createCourseName,
                _state.value.createCollegeName,
                _state.value.createPeriod
            )
            result.onSuccess { course ->
                _state.update { it.copy(generatedCourseCode = course.accessCode) }
                loadCourses()
                onSuccess()
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun loadCourseDetail(courseId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val course = getCourseDetailUseCase(courseId)
            _state.update { it.copy(
                selectedCourse = course, 
                isLoading = false,
                editName = course?.name ?: "",
                editPeriod = course?.period ?: ""
            ) }
            getStudentsByCourseUseCase(courseId).collect { students ->
                _state.update { it.copy(selectedCourseStudents = students) }
            }
        }
    }

    fun onEditNameChange(value: String) = _state.update { it.copy(editName = value) }
    fun onEditPeriodChange(value: String) = _state.update { it.copy(editPeriod = value) }
    fun toggleEditMode() = _state.update { it.copy(isEditMode = !it.isEditMode) }

    fun updateCourse(onSuccess: () -> Unit) {
        val current = _state.value.selectedCourse ?: return
        viewModelScope.launch {
            val updated = current.copy(
                name = _state.value.editName,
                period = _state.value.editPeriod
            )
            updateCourseUseCase(updated).onSuccess {
                _state.update { it.copy(selectedCourse = updated, isEditMode = false) }
                loadCourses()
                onSuccess()
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun deleteCourse(courseId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteCourseUseCase(courseId).onSuccess {
                loadCourses()
                onSuccess()
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = error.message) }
            }
        }
    }
}
