package com.example.docenteapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.docenteapp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DocenteApp",
        ) {
            App()
        }
    }
}