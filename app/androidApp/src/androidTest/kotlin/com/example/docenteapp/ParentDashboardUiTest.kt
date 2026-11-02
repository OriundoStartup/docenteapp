package com.example.docenteapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.ui.screens.ParentDashboard
import org.junit.Rule
import org.junit.Test

class ParentDashboardUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testEmptyPupilsMessage() {
        composeTestRule.setContent {
            ParentDashboard(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                students = emptyList(),
                recentActivities = emptyList(),
                onMenuClick = {},
                onLogout = {},
                onViewProfile = {},
                onEditProfile = {},
                onCommunicationsClick = {},
                onLinkStudentClick = {},
                onStudentClick = {}
            )
        }

        composeTestRule.onNodeWithText("Aún no tienes pupilos vinculados.").assertIsDisplayed()
    }

    @Test
    fun testPupilListDisplay() {
        val dummyPupils = listOf(
            Student("1", "Juanito", "Pérez", "20.123.456-7", "course1", "parent1")
        )

        composeTestRule.setContent {
            ParentDashboard(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                students = dummyPupils,
                recentActivities = emptyList(),
                onMenuClick = {},
                onLogout = {},
                onViewProfile = {},
                onEditProfile = {},
                onCommunicationsClick = {},
                onLinkStudentClick = {},
                onStudentClick = {}
            )
        }

        composeTestRule.onNodeWithText("Juanito Pérez").assertIsDisplayed()
        composeTestRule.onNodeWithText("RUT: 20.123.456-7").assertIsDisplayed()
    }

    @Test
    fun testVincularButtonClick() {
        var linkClicked = false

        composeTestRule.setContent {
            ParentDashboard(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                students = emptyList(),
                recentActivities = emptyList(),
                onMenuClick = {},
                onLogout = {},
                onViewProfile = {},
                onEditProfile = {},
                onCommunicationsClick = {},
                onLinkStudentClick = { linkClicked = true },
                onStudentClick = {}
            )
        }

        composeTestRule.onNodeWithText("Vincular").performClick()
        assert(linkClicked)
    }
}
