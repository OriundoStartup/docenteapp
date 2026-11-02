package com.example.docenteapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.example.docenteapp.ui.screens.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testLoginButtonValidation() {
        composeTestRule.setContent {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            
            LoginScreen(
                emailState = email,
                onEmailChange = { email = it },
                passwordState = password,
                onPasswordChange = { password = it },
                onLoginClick = {},
                onGoogleLoginClick = {},
                onNavigateToRegister = {}
            )
        }

        // Initially button should be disabled
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()

        // Enter partial credentials
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("user@test.com")
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()

        // Enter password
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")
        
        // Now button should be enabled
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsEnabled()
    }
    
    @Test
    fun testNavigationToRegister() {
        var navigated = false
        composeTestRule.setContent {
            LoginScreen(
                emailState = "",
                onEmailChange = {},
                passwordState = "",
                onPasswordChange = {},
                onLoginClick = {},
                onGoogleLoginClick = {},
                onNavigateToRegister = { navigated = true }
            )
        }

        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate aquí").performClick()
        assert(navigated)
    }
}
