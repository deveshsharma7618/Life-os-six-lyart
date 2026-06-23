package com.deveshsharma.lifeossixlyart.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SanctuaryColorScheme = darkColorScheme(
    primary = SanctuaryGold,
    onPrimary = SanctuaryDark,
    primaryContainer = SanctuaryGoldDim,
    onPrimaryContainer = White,
    secondary = SanctuaryTan,
    onSecondary = SanctuaryDark,
    background = SanctuaryDark,
    onBackground = SanctuaryTan,
    surface = SanctuaryCardBg,
    onSurface = SanctuaryTan,
    error = SanctuaryError,
    onError = White
)

@Composable
fun LifeOsSixLyartTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SanctuaryColorScheme,
        typography = Typography,
        content = content
    )
}
