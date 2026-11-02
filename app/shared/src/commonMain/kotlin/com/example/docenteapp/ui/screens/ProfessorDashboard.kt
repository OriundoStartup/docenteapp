package com.example.docenteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.docenteapp.domain.models.Course
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.ui.components.ActionButton
import com.example.docenteapp.ui.components.DashboardCard
import com.example.docenteapp.ui.components.SectionHeader

@Composable
fun ProfessorDashboard(
    drawerState: DrawerState,
    courses: List<Course>,
    courseStudentsMap: Map<String, List<Student>> = emptyMap(),
    onMenuClick: () -> Unit,
    onLogout: () -> Unit,
    onViewProfile: () -> Unit,
    onEditProfile: () -> Unit,
    onCreateCourseClick: () -> Unit,
    onAttendanceClick: (Course) -> Unit,
    onGradesClick: (Course) -> Unit,
    onCourseClick: (Course) -> Unit,
    onCommunicationsClick: () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Menú Docente",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Información Personal") },
                    selected = false,
                    onClick = onViewProfile,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Editar Perfil") },
                    selected = false,
                    onClick = onEditProfile,
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) }
                )
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = onLogout,
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onMenuClick) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú"
                            )
                        }
                        
                        Text(
                            text = "Panel del Profesor",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onCreateCourseClick,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Nuevo Curso") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    SectionHeader(title = "Acciones Rápidas")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionButton(
                            text = "Asistencia",
                            icon = "📝",
                            onClick = { 
                                courses.firstOrNull()?.let { onAttendanceClick(it) }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        ActionButton(
                            text = "Notas",
                            icon = "📊",
                            onClick = { 
                                if (courses.isNotEmpty()) onGradesClick(courses.first())
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "Comunicaciones",
                        icon = "📧",
                        onClick = onCommunicationsClick,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(title = "Mis Cursos")
                }

                if (courses.isEmpty()) {
                    item {
                        Text(
                            "No tienes cursos creados. ¡Crea el primero!",
                            modifier = Modifier.padding(vertical = 32.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(courses) { course ->
                        val students = courseStudentsMap[course.id] ?: emptyList()
                        val studentCountText = if (students.isEmpty()) "Sin alumnos vinculados" else "${students.size} alumno(s) vinculado(s)"

                        DashboardCard(
                            title = course.name,
                            subtitle = "${course.collegeId} - Cód: ${course.accessCode}\n👨‍🎓 $studentCountText",
                            icon = "📚",
                            onClick = { onCourseClick(course) },
                            actions = {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(
                                        onClick = { onAttendanceClick(course) },
                                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text("Asistencia")
                                    }
                                    TextButton(
                                        onClick = { onGradesClick(course) },
                                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text("Notas")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
