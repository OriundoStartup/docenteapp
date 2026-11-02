package com.example.docenteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.docenteapp.domain.models.Attendance
import com.example.docenteapp.domain.models.AttendanceStatus
import com.example.docenteapp.domain.models.CertificateType
import com.example.docenteapp.domain.models.GradeWithEvaluation
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.ui.components.SectionHeader

@Composable
fun StudentDetailScreen(
    student: Student,
    attendanceHistory: List<Attendance>,
    gradeHistory: List<GradeWithEvaluation>,
    onGenerateCertificate: (CertificateType) -> Unit,
    onBackClick: () -> Unit
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
                    Text(
                        text = "Detalle del Estudiante",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("👦", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("${student.firstName} ${student.lastName}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text("RUT: ${student.rut}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                
                SectionHeader(title = "Documentos")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onGenerateCertificate(CertificateType.ENROLLMENT) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Matrícula", style = MaterialTheme.typography.labelSmall)
                    }
                    Button(
                        onClick = { onGenerateCertificate(CertificateType.GRADES_REPORT) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text("Notas", style = MaterialTheme.typography.labelSmall)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(title = "Resumen de Asistencia")
                val presentCount = attendanceHistory.count { it.status == AttendanceStatus.PRESENT }
                val absentCount = attendanceHistory.count { it.status == AttendanceStatus.ABSENT }
                val lateCount = attendanceHistory.count { it.status == AttendanceStatus.LATE }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    AttendanceStat(label = "Presente", value = presentCount.toString(), color = MaterialTheme.colorScheme.primary)
                    AttendanceStat(label = "Ausente", value = absentCount.toString(), color = MaterialTheme.colorScheme.error)
                    AttendanceStat(label = "Atraso", value = lateCount.toString(), color = Color(0xFFFFC107))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(title = "Historial de Calificaciones")
            }

            if (gradeHistory.isEmpty()) {
                item {
                    Text("No hay calificaciones registradas.", modifier = Modifier.padding(vertical = 16.dp))
                }
            } else {
                items(gradeHistory) { item ->
                    GradeItem(
                        subject = item.evaluation.description,
                        value = item.grade.value.toString()
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = color)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun GradeItem(subject: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = subject, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}
