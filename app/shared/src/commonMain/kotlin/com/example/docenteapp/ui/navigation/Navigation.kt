package com.example.docenteapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.docenteapp.domain.models.UserRole
import com.example.docenteapp.ui.screens.AttendanceScreen
import com.example.docenteapp.ui.screens.CertificateViewScreen
import com.example.docenteapp.ui.screens.CommunicationsScreen
import com.example.docenteapp.ui.screens.CourseDetailScreen
import com.example.docenteapp.ui.screens.CreateCommunicationScreen
import com.example.docenteapp.ui.screens.CreateCourseScreen
import com.example.docenteapp.ui.screens.EditProfileScreen
import com.example.docenteapp.ui.screens.GradesScreen
import com.example.docenteapp.ui.screens.LinkStudentScreen
import com.example.docenteapp.ui.screens.LoginScreen
import com.example.docenteapp.ui.screens.MessageDetailScreen
import com.example.docenteapp.ui.screens.ParentDashboard
import com.example.docenteapp.ui.screens.ProfessorDashboard
import com.example.docenteapp.ui.screens.ProfileScreen
import com.example.docenteapp.ui.screens.RegisterScreen
import com.example.docenteapp.ui.screens.StudentDetailScreen
import com.example.docenteapp.ui.viewmodels.AttendanceViewModel
import com.example.docenteapp.ui.viewmodels.AuthViewModel
import com.example.docenteapp.ui.viewmodels.CommunicationViewModel
import com.example.docenteapp.ui.viewmodels.CourseViewModel
import com.example.docenteapp.ui.viewmodels.LinkStudentViewModel
import com.example.docenteapp.ui.viewmodels.ParentViewModel
import com.example.docenteapp.ui.viewmodels.StudentDetailViewModel
import com.example.docenteapp.ui.viewmodels.UserSessionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object Register : Route
    @Serializable data object Home : Route
    @Serializable data object Profile : Route
    @Serializable data object EditProfile : Route
    @Serializable data object CreateCourse : Route
    @Serializable data class CourseDetail(val courseId: String) : Route
    @Serializable data class Attendance(val courseId: String, val courseName: String) : Route
    @Serializable data class Grades(val courseId: String, val courseName: String) : Route
    @Serializable data object Communications : Route
    @Serializable data object CreateCommunication : Route
    @Serializable data object LinkStudent : Route
    @Serializable data class StudentDetail(val studentId: String) : Route
    @Serializable data class MessageDetail(val messageId: String) : Route
    @Serializable data object CertificateView : Route
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login
    ) {
        composable<Route.Login> {
            val viewModel: AuthViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            
            LoginScreen(
                emailState = state.loginEmail,
                onEmailChange = viewModel::onLoginEmailChange,
                passwordState = state.loginPassword,
                onPasswordChange = viewModel::onLoginPasswordChange,
                errorMessage = state.errorMessage,
                onLoginClick = {
                    viewModel.login {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    }
                },
                onGoogleLoginClick = {},
                onNavigateToRegister = { navController.navigate(Route.Register) }
            )
        }
        
        composable<Route.Register> {
            val viewModel: AuthViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            RegisterScreen(
                nameState = state.registerName,
                onNameChange = viewModel::onRegisterNameChange,
                emailState = state.registerEmail,
                onEmailChange = viewModel::onRegisterEmailChange,
                passwordState = state.registerPassword,
                onPasswordChange = viewModel::onRegisterPasswordChange,
                specialtyState = state.registerSpecialty,
                onSpecialtyChange = viewModel::onRegisterSpecialtyChange,
                phoneState = state.registerPhone,
                onPhoneChange = viewModel::onRegisterPhoneChange,
                selectedRole = state.registerRole,
                onRoleChange = viewModel::onRegisterRoleChange,
                onRegisterClick = {
                    viewModel.register {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        
        composable<Route.Home> {
            val sessionVm: UserSessionViewModel = koinViewModel()
            
            // Garantizar la recarga de sesión fresca al volver al Home
            LaunchedEffect(Unit) {
                sessionVm.loadSession()
            }

            val sessionState by sessionVm.state.collectAsState()
            val currentUser = sessionState.currentUser

            if (currentUser?.role == UserRole.PARENT) {
                val parentVm: ParentViewModel = koinViewModel()
                val parentState by parentVm.state.collectAsState()

                ParentDashboard(
                    drawerState = drawerState,
                    students = parentState.students,
                    recentActivities = parentState.recentActivities,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onLogout = { 
                        sessionVm.logout {
                            navController.navigate(Route.Login) {
                                popUpTo(Route.Home) { inclusive = true }
                            }
                        }
                    },
                    onViewProfile = { navController.navigate(Route.Profile) },
                    onEditProfile = { navController.navigate(Route.EditProfile) },
                    onCommunicationsClick = { navController.navigate(Route.Communications) },
                    onLinkStudentClick = { navController.navigate(Route.LinkStudent) },
                    onStudentClick = { student -> navController.navigate(Route.StudentDetail(student.id)) }
                )
            } else {
                val courseVm: CourseViewModel = koinViewModel()
                
                LaunchedEffect(Unit) {
                    courseVm.loadCourses()
                }

                val courseState by courseVm.state.collectAsState()

                ProfessorDashboard(
                    drawerState = drawerState,
                    courses = courseState.courses,
                    courseStudentsMap = courseState.courseStudentsMap,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onLogout = { 
                        sessionVm.logout {
                            navController.navigate(Route.Login) {
                                popUpTo(Route.Home) { inclusive = true }
                            }
                        }
                    },
                    onViewProfile = { navController.navigate(Route.Profile) },
                    onEditProfile = { navController.navigate(Route.EditProfile) },
                    onCreateCourseClick = { navController.navigate(Route.CreateCourse) },
                    onCourseClick = { course -> navController.navigate(Route.CourseDetail(course.id)) },
                    onAttendanceClick = { course -> navController.navigate(Route.Attendance(course.id, course.name)) },
                    onGradesClick = { course -> navController.navigate(Route.Grades(course.id, course.name)) },
                    onCommunicationsClick = { navController.navigate(Route.Communications) }
                )
            }
        }

        composable<Route.Attendance> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.Attendance>()
            val viewModel: AttendanceViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(route.courseId) {
                viewModel.initialize(route.courseId, route.courseName)
            }

            AttendanceScreen(
                courseName = route.courseName,
                date = state.date,
                students = state.students,
                attendanceMap = state.attendanceMap,
                onStatusChange = viewModel::onStatusChange,
                onSaveClick = {
                    viewModel.saveAttendance {
                        navController.popBackStack()
                    }
                },
                onBackClick = { navController.popBackStack() },
                isSaving = state.isSaving,
                isSavedSuccess = state.isSavedSuccess,
                errorMessage = state.errorMessage
            )
        }

        composable<Route.Grades> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.Grades>()
            GradesScreen(
                courseName = route.courseName,
                evaluationDescription = "",
                onDescriptionChange = {},
                students = emptyList(),
                gradesMap = emptyMap(),
                onGradeChange = { _, _ -> },
                onSaveClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.Communications> {
            val sessionVm: UserSessionViewModel = koinViewModel()
            
            LaunchedEffect(Unit) {
                sessionVm.loadSession()
            }

            val sessionState by sessionVm.state.collectAsState()
            val role = sessionState.currentUser?.role ?: UserRole.PROFESSOR

            val viewModel: CommunicationViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadInbox()
            }

            CommunicationsScreen(
                userRole = role,
                communications = state.communications,
                onBackClick = { navController.popBackStack() },
                onCreateClick = { navController.navigate(Route.CreateCommunication) },
                onMessageClick = { comm -> navController.navigate(Route.MessageDetail(comm.id)) }
            )
        }

        composable<Route.CreateCommunication> {
            val sessionVm: UserSessionViewModel = koinViewModel()
            val sessionState by sessionVm.state.collectAsState()
            val role = sessionState.currentUser?.role ?: UserRole.PROFESSOR

            val viewModel: CommunicationViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(role) {
                viewModel.loadFormContext(role)
            }

            CreateCommunicationScreen(
                userRole = role,
                titleState = state.commTitle,
                onTitleChange = viewModel::onCommTitleChange,
                contentState = state.commContent,
                onContentChange = viewModel::onCommContentChange,
                receiverIdState = state.commReceiverId,
                onReceiverIdChange = viewModel::onCommReceiverChange,
                courses = state.availableCourses,
                students = state.availableStudents,
                professors = state.availableProfessors,
                onSendClick = {
                    viewModel.sendCommunication {
                        navController.popBackStack()
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.MessageDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.MessageDetail>()
            val viewModel: CommunicationViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadInbox()
            }

            val message = state.communications.find { it.id == route.messageId }

            if (message != null) {
                MessageDetailScreen(
                    message = message,
                    onBackClick = { navController.popBackStack() },
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        composable<Route.CourseDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.CourseDetail>()
            val viewModel: CourseViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(route.courseId) {
                viewModel.loadCourseDetail(route.courseId)
            }

            CourseDetailScreen(
                course = state.selectedCourse,
                students = state.selectedCourseStudents,
                isLoading = state.isLoading,
                isEditMode = state.isEditMode,
                editName = state.editName,
                editPeriod = state.editPeriod,
                onEditNameChange = viewModel::onEditNameChange,
                onEditPeriodChange = viewModel::onEditPeriodChange,
                onToggleEdit = viewModel::toggleEditMode,
                onSaveClick = {
                    viewModel.updateCourse {
                        // Opcional: mostrar snackbar o volver atrás
                    }
                },
                onDeleteClick = {
                    viewModel.deleteCourse(route.courseId) {
                        navController.popBackStack()
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.CreateCourse> {
            val viewModel: CourseViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            CreateCourseScreen(
                courseName = state.createCourseName,
                onCourseNameChange = viewModel::onCourseNameChange,
                collegeName = state.createCollegeName,
                onCollegeNameChange = viewModel::onCollegeNameChange,
                period = state.createPeriod,
                onPeriodChange = viewModel::onPeriodChange,
                generatedCode = state.generatedCourseCode,
                errorMessage = state.errorMessage,
                onCreateClick = {
                    if (state.generatedCourseCode.isEmpty()) {
                        viewModel.createCourse {}
                    } else {
                        navController.popBackStack()
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.LinkStudent> {
            val viewModel: LinkStudentViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LinkStudentScreen(
                accessCode = state.accessCode,
                onAccessCodeChange = viewModel::onAccessCodeChange,
                firstName = state.firstName,
                onFirstNameChange = viewModel::onFirstNameChange,
                lastName = state.lastName,
                onLastNameChange = viewModel::onLastNameChange,
                rut = state.rut,
                onRutChange = viewModel::onRutChange,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onLinkClick = {
                    viewModel.linkStudent {
                        navController.popBackStack()
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.StudentDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.StudentDetail>()
            val vm: StudentDetailViewModel = koinViewModel()
            val state by vm.state.collectAsState()
            
            LaunchedEffect(route.studentId) {
                vm.loadStudentDetail(route.studentId)
            }
            
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                state.selectedStudent?.let { student ->
                    StudentDetailScreen(
                        student = student,
                        attendanceHistory = state.attendanceHistory,
                        gradeHistory = emptyList(),
                        onGenerateCertificate = { type ->
                            vm.generateReport(type) {
                                navController.navigate(Route.CertificateView)
                            }
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                } ?: run {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se encontró la información del estudiante")
                    }
                }
            }
        }

        composable<Route.CertificateView> {
            val vm: StudentDetailViewModel = koinViewModel()
            val state by vm.state.collectAsState()
            
            state.generatedCertificate?.let { cert ->
                CertificateViewScreen(
                    certificate = cert,
                    onBackClick = { navController.popBackStack() },
                    onShareClick = {}
                )
            } ?: run {
                Text("No hay certificado para mostrar")
            }
        }

        composable<Route.Profile> {
            val sessionVm: UserSessionViewModel = koinViewModel()
            val sessionState by sessionVm.state.collectAsState()
            
            ProfileScreen(
                user = sessionState.currentUser,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.EditProfile> {
            val sessionVm: UserSessionViewModel = koinViewModel()
            val sessionState by sessionVm.state.collectAsState()
            val user = sessionState.currentUser
            
            // Usamos state local para la edición por ahora
            var name by remember {
                mutableStateOf(user?.let { "${it.firstName} ${it.lastName}" } ?: "")
            }
            var email by remember {
                mutableStateOf(user?.email ?: "")
            }

            EditProfileScreen(
                nameState = name,
                onNameChange = { name = it },
                emailState = email,
                onEmailChange = { email = it },
                onSaveClick = { 
                    // Simulación de guardado
                    navController.popBackStack() 
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
