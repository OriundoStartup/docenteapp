package com.example.docenteapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docenteapp.domain.models.Certificate
import com.example.docenteapp.domain.models.CertificateType

@Composable
fun CertificateViewScreen(
    certificate: Certificate,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Documento Oficial") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "REPUBLICA DE CHILE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "MINISTERIO DE EDUCACIÓN",
                        style = MaterialTheme.typography.labelSmall
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = when(certificate.type) {
                            CertificateType.ENROLLMENT -> "CERTIFICADO DE ALUMNO REGULAR"
                            CertificateType.GRADES_REPORT -> "INFORME DE CALIFICACIONES"
                            CertificateType.ATTENDANCE_SUMMARY -> "INFORME DE ASISTENCIA"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Text(
                        text = certificate.content,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Justify,
                        fontFamily = FontFamily.Serif
                    )
                    
                    Spacer(modifier = Modifier.height(64.dp))
                    
                    HorizontalDivider(modifier = Modifier.width(200.dp))
                    Text(
                        "Firma del Director",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        "DocenteApp Cloud System",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Fecha: ${certificate.issueDate}", style = MaterialTheme.typography.labelSmall)
                            Text("Código: ${certificate.verificationCode}", style = MaterialTheme.typography.labelSmall)
                        }
                        Text("QR VERIFIED", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onShareClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Exportar como PDF")
            }
        }
    }
}
