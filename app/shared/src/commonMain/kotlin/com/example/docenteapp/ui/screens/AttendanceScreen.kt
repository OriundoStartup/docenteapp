package com.example.docenteapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docenteapp.domain.models.AttendanceStatus
import com.example.docenteapp.domain.models.Student
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
fun AttendanceScreen(
    courseName: String,
    date: LocalDate,
    students: List<Student>,
    attendanceMap: Map<String, AttendanceStatus>,
    onStatusChange: (String, AttendanceStatus) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    isSaving: Boolean = false,
    isSavedSuccess: Boolean = false,
    errorMessage: String? = null
) {
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
                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text(
                            "Pasar Asistencia",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            courseName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(
                        onClick = onSaveClick,
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            Text("GUARDANDO...", fontWeight = FontWeight.Bold)
                        } else {
                            Text("GUARDAR", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))

                        val dayNum = date.day
                        val monthNum = date.month.number
                        
                        val dayStr = if (dayNum < 10) "0$dayNum" else "$dayNum"
                        val monthStr = if (monthNum < 10) "0$monthNum" else "$monthNum"
                        
                        Text(
                            text = "Fecha: $dayStr/$monthStr/${date.year}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(students) { student ->
                        StudentAttendanceItem(
                            student = student,
                            currentStatus = attendanceMap[student.id] ?: AttendanceStatus.PRESENT,
                            onStatusChange = { status -> onStatusChange(student.id, status) }
                        )
                    }
                }
            }

            // Overlay for success/error messages
            if (isSavedSuccess || errorMessage != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    color = if (isSavedSuccess) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSavedSuccess) "Asistencia guardada con éxito" else "Error: $errorMessage",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentAttendanceItem(
    student: Student,
    currentStatus: AttendanceStatus,
    onStatusChange: (AttendanceStatus) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${student.firstName} ${student.lastName}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AttendanceOption(
                    label = "P",
                    isSelected = currentStatus == AttendanceStatus.PRESENT,
                    color = Color(0xFF4CAF50),
                    onClick = { onStatusChange(AttendanceStatus.PRESENT) }
                )
                AttendanceOption(
                    label = "A",
                    isSelected = currentStatus == AttendanceStatus.ABSENT,
                    color = Color(0xFFF44336),
                    onClick = { onStatusChange(AttendanceStatus.ABSENT) }
                )
                AttendanceOption(
                    label = "T",
                    isSelected = currentStatus == AttendanceStatus.LATE,
                    color = Color(0xFFFFC107),
                    onClick = { onStatusChange(AttendanceStatus.LATE) }
                )
                AttendanceOption(
                    label = "J",
                    isSelected = currentStatus == AttendanceStatus.JUSTIFIED,
                    color = Color(0xFF2196F3),
                    onClick = { onStatusChange(AttendanceStatus.JUSTIFIED) }
                )
            }
        }
    }
}

@Composable
fun AttendanceOption(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) color else color.copy(alpha = 0.1f),
        contentColor = if (isSelected) Color.White else color,
        border = if (!isSelected) BorderStroke(1.dp, color) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = label, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        }
    }
}
