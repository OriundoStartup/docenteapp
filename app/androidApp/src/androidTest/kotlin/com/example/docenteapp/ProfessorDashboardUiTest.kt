package com.example.docenteapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.example.docenteapp.domain.models.Course
import com.example.docenteapp.ui.screens.ProfessorDashboard
import org.junit.Rule
import org.junit.Test

class ProfessorDashboardUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testEmptyStateMessage() {
        composeTestRule.setContent {
            ProfessorDashboard(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                courses = emptyList(),
                onMenuClick = {},
                onLogout = {},
                onViewProfile = {},
                onEditProfile = {},
                onCreateCourseClick = {},
                onAttendanceClick = {},
                onGradesClick = {},
                onCourseClick = {},
                onCommunicationsClick = {}
            )
        }

        composeTestRule.onNodeWithText("No tienes cursos creados. ¡Crea el primero!").assertIsDisplayed()
    }

    @Test
    fun testCourseListDisplay() {
        val dummyCourses = listOf(
            Course("1", "Matemáticas 8vo", "prof@test.com", "San Pedro", "ABC123", "2024")
        )

        composeTestRule.setContent {
            ProfessorDashboard(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                courses = dummyCourses,
                onMenuClick = {},
                onLogout = {},
                onViewProfile = {},
                onEditProfile = {},
                onCreateCourseClick = {},
                onAttendanceClick = {},
                onGradesClick = {},
                onCourseClick = {},
                onCommunicationsClick = {}
            )
        }

        composeTestRule.onNodeWithText("Matemáticas 8vo").assertIsDisplayed()
        composeTestRule.onNodeWithText("San Pedro - Cód: ABC123", substring = true).assertIsDisplayed()
    }

    @Test
    fun testNavigationClicks() {
        var createClicked = false
        var commsClicked = false

        composeTestRule.setContent {
            ProfessorDashboard(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                courses = emptyList(),
                onMenuClick = {},
                onLogout = {},
                onViewProfile = {},
                onEditProfile = {},
                onCreateCourseClick = { createClicked = true },
                onAttendanceClick = {},
                onGradesClick = {},
                onCourseClick = {},
                onCommunicationsClick = { commsClicked = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Redactar", ignoreCase = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("Nuevo Curso").performClick()
        assert(createClicked)

        composeTestRule.onNodeWithText("Comunicaciones").performClick()
        assert(commsClicked)
    }
}
