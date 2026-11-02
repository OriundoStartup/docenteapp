package com.example.docenteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.docenteapp.domain.models.RecentActivity
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.ui.components.ActionButton
import com.example.docenteapp.ui.components.DashboardCard
import com.example.docenteapp.ui.components.SectionHeader

@Composable
fun ParentDashboard(
    drawerState: DrawerState,
    students: List<Student>,
    recentActivities: List<RecentActivity>,
    onMenuClick: () -> Unit,
    onLogout: () -> Unit,
    onViewProfile: () -> Unit,
    onEditProfile: () -> Unit,
    onCommunicationsClick: () -> Unit,
    onLinkStudentClick: () -> Unit,
    onStudentClick: (Student) -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Menú Apoderado",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Mi Información") },
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
                    color = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
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
                            text = "Panel del Apoderado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    SectionHeader(title = "Gestión")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionButton(
                            text = "Vincular",
                            icon = "🔑",
                            onClick = onLinkStudentClick,
                            modifier = Modifier.weight(1f),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                        ActionButton(
                            text = "Mensajes",
                            icon = "📧",
                            onClick = onCommunicationsClick,
                            modifier = Modifier.weight(1f),
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(title = "Mis Pupilos")
                }

                if (students.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Aún no tienes pupilos vinculados.", style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Usa el código de acceso entregado por el profesor para vincular un estudiante.", 
                                    style = MaterialTheme.typography.bodyMedium, 
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(students) { student ->
                        DashboardCard(
                            title = "${student.firstName} ${student.lastName}",
                            subtitle = "RUT: ${student.rut}",
                            icon = "👦",
                            onClick = { onStudentClick(student) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(title = "Actividad Reciente")
                }

                if (recentActivities.isEmpty()) {
                    item {
                        Text(
                            "No hay actividad reciente para mostrar.",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(recentActivities) { activity ->
                        DashboardCard(
                            title = activity.title,
                            subtitle = activity.description,
                            icon = when (activity.type) {
                                "GRADE" -> "📊"
                                "ATTENDANCE" -> "📝"
                                "COMMUNICATION" -> "📧"
                                else -> "🔔"
                            },
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}
