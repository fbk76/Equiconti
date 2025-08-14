package com.cz.equiconti.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Olive = Color(0xFF556B2F)       // verde oliva
private val OliveDark = Color(0xFF445626)

private val LightColors = lightColorScheme(
    primary = Olive,
    onPrimary = Color.White,
    primaryContainer = Olive.copy(alpha = 0.85f),
    onPrimaryContainer = Color.White,
    background = Olive.copy(alpha = 0.06f),
    surface = Color.White,
    onSurface = Color(0xFF111111),
)

private val DarkColors = darkColorScheme(
    primary = Olive,
    onPrimary = Color.White,
    primaryContainer = OliveDark,
    onPrimaryContainer = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF161616),
    onSurface = Color(0xFFEDEDED),
)

@Composable
fun EquicontiTheme(
    darkTheme: Boolean = false,
    colorScheme: ColorScheme = if (darkTheme) DarkColors else LightColors,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
