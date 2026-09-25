package com.example.taskflow.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF4433D7),
    secondary = Color(0xFF55D7EF),
    background = Color(0xFFF8F8FC),
    surface = Color.White,
    onPrimary = Color.White,
    surfaceVariant = Color(0xFFE8EEFC),
    onBackground = Color(0xFF1C2340),
    onSurface = Color(0xFF1C2340),
    onSurfaceVariant = Color(0xFF52628B)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9B90FF),
    secondary = Color(0xFF66DCEF),

    background = Color(0xFF101114),
    surface = Color(0xFF1A1B20),
    surfaceVariant = Color(0xFF292B33),

    onPrimary = Color.Black,
    onBackground = Color(0xFFF2F2F2),
    onSurface = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFFB9BBC5)
)

@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColors
        },
        typography = Typography,
        content = content
    )
}