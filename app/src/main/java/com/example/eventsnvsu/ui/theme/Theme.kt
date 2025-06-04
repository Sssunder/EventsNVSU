package com.example.eventsnvsu.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val iOSLightColorScheme = lightColorScheme(
    primary = iOSPrimary,
    onPrimary = iOSOnPrimary,
    background = iOSBackground,
    onBackground = iOSOnBackground,
    surface = iOSSurface,
    onSurface = iOSOnSurface,
    secondary = iOSPrimary,
    onSecondary = iOSOnPrimary,
    tertiary = iOSPrimary,
    onTertiary = iOSOnPrimary,
)

@Composable
fun EventsNVSUTheme(
    darkTheme: Boolean = false, // iOS стиль всегда светлый
    dynamicColor: Boolean = false, // отключаем динамические цвета
    content: @Composable () -> Unit
) {
    val colorScheme = iOSLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

