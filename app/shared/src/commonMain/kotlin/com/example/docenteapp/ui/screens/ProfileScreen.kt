package com.example.docenteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.docenteapp.domain.models.User
import com.example.docenteapp.domain.models.UserRole

@Composable
fun ProfileScreen(
    user: User?,
    onBackClick: () -> Unit
) {
    val name = user?.let { "${it.firstName} ${it.lastName}" } ?: ""
    val email = user?.email ?: ""
    val role = user?.role ?: UserRole.PROFESSOR

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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                    
                    Text(
                        text = "Mi Información",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = if (role == UserRole.PROFESSOR) "Profesor" else "Apoderado",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Correo Electrónico", style = MaterialTheme.typography.labelMedium)
                    Text(email, style = MaterialTheme.typography.bodyLarge)
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    Text("Estado de la cuenta", style = MaterialTheme.typography.labelMedium)
                    Text("Activa", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)

                    if (role == UserRole.PROFESSOR) {
                        val prof = user?.professorProfile
                        if (prof != null) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            Text("Especialidad", style = MaterialTheme.typography.labelMedium)
                            Text(prof.specialty, style = MaterialTheme.typography.bodyLarge)
                            
                            prof.bio?.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Bio", style = MaterialTheme.typography.labelMedium)
                                Text(it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    if (role == UserRole.PARENT) {
                        val par = user?.parentProfile
                        if (par != null) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            Text("Teléfono de contacto", style = MaterialTheme.typography.labelMedium)
                            Text(par.phone, style = MaterialTheme.typography.bodyLarge)
                            
                            par.address?.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Dirección", style = MaterialTheme.typography.labelMedium)
                                Text(it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}
