package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LuxuryColorScheme = darkColorScheme(
    primary = ElectricSapphire,
    onPrimary = Color.White,
    secondary = MutedBlueGlow,
    onSecondary = MidnightBlue,
    tertiary = SapphireGlow,
    background = MidnightBlue,
    onBackground = SoftIceWhite,
    surface = RichNavy,
    onSurface = SoftIceWhite,
    surfaceVariant = DarkGreyNavy,
    onSurfaceVariant = SilverFrost,
    outline = MutedBlueGlow
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme for luxury look
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // We ignore dynamicColor and darkTheme to enforce the premium "Aether Call" midnight brand identity
    MaterialTheme(
        colorScheme = LuxuryColorScheme,
        typography = Typography,
        content = content
    )
}

