package com.example.docenteapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de colores moderna para DocenteApp
val Primary = Color(0xFF6200EE)
val PrimaryContainer = Color(0xFFBB86FC)
val Secondary = Color(0xFF03DAC6)
val Background = Color(0xFFF8F9FA)
val Surface = Color(0xFFFFFFFF)
val OnPrimary = Color.White
val OnBackground = Color(0xFF1C1B1F)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    background = Background,
    surface = Surface,
    onPrimary = OnPrimary,
    onBackground = OnBackground,
    onSurface = OnBackground
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryContainer,
    primaryContainer = Primary,
    secondary = Secondary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun DocenteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
