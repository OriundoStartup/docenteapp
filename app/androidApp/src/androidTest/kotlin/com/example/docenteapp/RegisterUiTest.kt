package com.example.docenteapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.example.docenteapp.domain.models.UserRole
import com.example.docenteapp.ui.screens.RegisterScreen
import org.junit.Rule
import org.junit.Test

class RegisterUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testRegisterButtonValidation() {
        composeTestRule.setContent {
            var name by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var specialty by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var role by remember { mutableStateOf(UserRole.PROFESSOR) }
            
            RegisterScreen(
                nameState = name,
                onNameChange = { name = it },
                emailState = email,
                onEmailChange = { email = it },
                passwordState = password,
                onPasswordChange = { password = it },
                specialtyState = specialty,
                onSpecialtyChange = { specialty = it },
                phoneState = phone,
                onPhoneChange = { phone = it },
                selectedRole = role,
                onRoleChange = { role = it },
                onRegisterClick = {},
                onNavigateToLogin = {}
            )
        }

        // Button should be disabled initially
        composeTestRule.onNodeWithText("Registrarse").assertIsNotEnabled()

        // Fill fields
        composeTestRule.onNodeWithText("Nombre Completo").performTextInput("Test User")
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@user.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")

        // Now button should be enabled
        composeTestRule.onNodeWithText("Registrarse").assertIsEnabled()
    }

    @Test
    fun testRoleSelection() {
        composeTestRule.setContent {
            var role by remember { mutableStateOf(UserRole.PROFESSOR) }
            RegisterScreen(
                nameState = "",
                onNameChange = {},
                emailState = "",
                onEmailChange = {},
                passwordState = "",
                onPasswordChange = {},
                specialtyState = "",
                onSpecialtyChange = {},
                phoneState = "",
                onPhoneChange = {},
                selectedRole = role,
                onRoleChange = { role = it },
                onRegisterClick = {},
                onNavigateToLogin = {}
            )
        }

        // Verify "Profesor" is selected by default
        composeTestRule.onNodeWithText("Profesor").assertIsSelected()
        composeTestRule.onNodeWithText("Apoderado").assertIsNotSelected()

        // Change to "Apoderado"
        composeTestRule.onNodeWithText("Apoderado").performClick()

        // Verify selection changed
        composeTestRule.onNodeWithText("Apoderado").assertIsSelected()
        composeTestRule.onNodeWithText("Profesor").assertIsNotSelected()
    }
}
