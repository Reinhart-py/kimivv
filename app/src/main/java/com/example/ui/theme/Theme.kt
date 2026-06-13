package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KimiColorScheme = lightColorScheme(
    primary = KimiBurgundy,
    onPrimary = Color.White,
    secondary = KimiPeach,
    onSecondary = KimiBurgundy,
    tertiary = KimiDarkPeach,
    background = KimiCream,
    onBackground = KimiCharcoal,
    surface = KimiWhite,
    onSurface = KimiCharcoal,
    surfaceVariant = KimiLightRose,
    onSurfaceVariant = KimiWarmGray,
    outline = KimiDarkPeach
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KimiColorScheme,
        typography = Typography,
        content = content
    )
}

