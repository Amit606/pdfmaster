package com.kwh.pdfrederall.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AllPdfReaderColorScheme = darkColorScheme(
    primary = BlueAccent,
    onPrimary = Color.White,
    primaryContainer = BlueGradientStart,
    onPrimaryContainer = Color.White,
    secondary = BlueLight,
    onSecondary = Color.White,
    background = NavyDeep,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
    error = RedAccent,
    onError = Color.White
)

@Composable
fun AllPdfReaderTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AllPdfReaderColorScheme,
        typography = AppTypography,
        content = content
    )
}
