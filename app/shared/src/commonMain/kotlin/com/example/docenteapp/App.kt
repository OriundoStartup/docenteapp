package com.example.docenteapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.docenteapp.ui.navigation.AppNavigation
import com.example.docenteapp.ui.theme.DocenteAppTheme
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        DocenteAppTheme {
            Scaffold { paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    AppNavigation(
                        navController = navController,
                        drawerState = drawerState,
                        scope = scope
                    )
                }
            }
        }
    }
}
