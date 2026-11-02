package com.example.docenteapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.example.docenteapp.domain.models.AttendanceStatus
import com.example.docenteapp.domain.models.Student
import com.example.docenteapp.ui.screens.AttendanceScreen
import kotlinx.datetime.LocalDate
import org.junit.Rule
import org.junit.Test

class AttendanceScreenUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testAttendanceSelection() {
        val dummyStudents = listOf(
            Student("1", "Juan", "Pérez", "123", "course1", "parent1")
        )

        composeTestRule.setContent {
            var attendanceMap by remember { mutableStateOf(mapOf("1" to AttendanceStatus.PRESENT)) }
            
            AttendanceScreen(
                courseName = "Matemáticas",
                date = LocalDate(2024, 9, 7),
                students = dummyStudents,
                attendanceMap = attendanceMap,
                onStatusChange = { id, status -> 
                    attendanceMap = attendanceMap.toMutableMap().apply { put(id, status) }
                },
                onSaveClick = {},
                onBackClick = {}
            )
        }

        // Verify student name is displayed
        composeTestRule.onNodeWithText("Juan Pérez").assertIsDisplayed()

        // Verify "P" (Present) is selected
        composeTestRule.onNodeWithText("P").assertIsDisplayed()
        composeTestRule.onNodeWithText("A").assertIsDisplayed()

        // Click on "A" (Absent)
        composeTestRule.onNodeWithText("A").performClick()
    }

    @Test
    fun testSaveButton() {
        var saved = false
        composeTestRule.setContent {
            AttendanceScreen(
                courseName = "Test",
                date = LocalDate(2024, 1, 1),
                students = emptyList(),
                attendanceMap = emptyMap(),
                onStatusChange = { _, _ -> },
                onSaveClick = { saved = true },
                onBackClick = {}
            )
        }

        composeTestRule.onNodeWithText("GUARDAR").performClick()
        assert(saved)
    }
}
