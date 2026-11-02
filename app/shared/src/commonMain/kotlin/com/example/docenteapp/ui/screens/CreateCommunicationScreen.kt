package com.example.docenteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.docenteapp.domain.models.Course
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.domain.models.User
import com.example.docenteapp.domain.models.UserRole

@Composable
fun CreateCommunicationScreen(
    userRole: UserRole,
    titleState: String,
    onTitleChange: (String) -> Unit,
    contentState: String,
    onContentChange: (String) -> Unit,
    receiverIdState: String,
    onReceiverIdChange: (String) -> Unit,
    courses: List<Course>,
    students: List<Student> = emptyList(),
    professors: List<User> = emptyList(),
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var isCommunityMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                    Text(
                        text = "Redactar Mensaje",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Tipo de Comunicado",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !isCommunityMessage,
                    onClick = { 
                        isCommunityMessage = false 
                        onReceiverIdChange("") 
                    },
                    label = { 
                        Text(if (userRole == UserRole.PROFESSOR) "Directo (Apoderado)" else "Directo (Profesor)") 
                    },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = isCommunityMessage,
                    onClick = { 
                        isCommunityMessage = true 
                        onReceiverIdChange("") 
                    },
                    label = { 
                        Text(if (userRole == UserRole.PROFESSOR) "Comunitario (Curso)" else "Comunitario (Grupo Padres)") 
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isCommunityMessage) {
                Text(
                    text = if (userRole == UserRole.PROFESSOR) "Selecciona al estudiante:" else "Selecciona al profesor:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (userRole == UserRole.PROFESSOR) {
                        items(students) { student ->
                            val isSelected = receiverIdState == student.parentId
                            InputChip(
                                selected = isSelected,
                                onClick = { onReceiverIdChange(student.parentId) },
                                label = { Text("${student.firstName} ${student.lastName}") },
                                trailingIcon = if (isSelected) { { Text("✓") } } else null
                            )
                        }
                    } else {
                        items(professors) { professor ->
                            val isSelected = receiverIdState == professor.id
                            InputChip(
                                selected = isSelected,
                                onClick = { onReceiverIdChange(professor.id) },
                                label = { Text("${professor.firstName} ${professor.lastName}") },
                                trailingIcon = if (isSelected) { { Text("✓") } } else null
                            )
                        }
                    }
                }
            } else {
                Text(
                    "Selecciona el curso destinatario:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(courses) { course ->
                        val isSelected = receiverIdState == course.id
                        InputChip(
                            selected = isSelected,
                            onClick = { onReceiverIdChange(course.id) },
                            label = { Text(course.name) },
                            trailingIcon = if (isSelected) { { Text("✓") } } else null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = titleState,
                onValueChange = onTitleChange,
                label = { Text("Asunto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contentState,
                onValueChange = onContentChange,
                label = { Text("Mensaje") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                minLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSendClick,
                enabled = receiverIdState.isNotEmpty() && titleState.isNotEmpty() && contentState.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Enviar Comunicado", fontWeight = FontWeight.Bold)
            }
        }
    }
}
