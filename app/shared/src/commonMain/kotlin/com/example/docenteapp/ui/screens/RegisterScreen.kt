package com.example.docenteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docenteapp.domain.models.UserRole

@Composable
fun RegisterScreen(
    nameState: String,
    onNameChange: (String) -> Unit,
    emailState: String,
    onEmailChange: (String) -> Unit,
    passwordState: String,
    onPasswordChange: (String) -> Unit,
    specialtyState: String,
    onSpecialtyChange: (String) -> Unit,
    phoneState: String,
    onPhoneChange: (String) -> Unit,
    selectedRole: UserRole,
    onRoleChange: (UserRole) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Únete a la comunidad educativa",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Selector de Rol (Estable)
            Text(
                text = "Selecciona tu perfil:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRole == UserRole.PROFESSOR,
                    onClick = { onRoleChange(UserRole.PROFESSOR) }
                )
                Text("Profesor")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = selectedRole == UserRole.PARENT,
                    onClick = { onRoleChange(UserRole.PARENT) }
                )
                Text("Apoderado")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nameState,
                onValueChange = onNameChange,
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailState,
                onValueChange = onEmailChange,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordState,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedRole == UserRole.PROFESSOR) {
                OutlinedTextField(
                    value = specialtyState,
                    onValueChange = onSpecialtyChange,
                    label = { Text("Especialidad (ej: Matemáticas)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                OutlinedTextField(
                    value = phoneState,
                    onValueChange = onPhoneChange,
                    label = { Text("Teléfono de Contacto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRegisterClick,
                enabled = nameState.isNotEmpty() && emailState.isNotEmpty() && passwordState.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}
