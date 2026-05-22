package com.kimseongwooo.pawming.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object PawmingColors {
    val Primary300 = Color(0xFFF2A882)
    val Primary400 = Color(0xFFED9272)
    val Primary500 = Color(0xFFE8734A)
    val Primary600 = Color(0xFFCF5E37)
    val Primary700 = Color(0xFFB54A26)

    val Secondary400 = Color(0xFF8C7B6D)
    val Secondary500 = Color(0xFF7C6B5D)
    val Secondary600 = Color(0xFF6B5D4F)

    val Neutral50  = Color(0xFFFAF9F7)
    val Neutral100 = Color(0xFFF2F0EC)
    val Neutral200 = Color(0xFFE5E0D9)
    val Neutral400 = Color(0xFFBBB4AA)
    val Neutral500 = Color(0xFF9E9589)
    val Neutral700 = Color(0xFF5C5650)
    val Neutral900 = Color(0xFF1E1B18)

    val Success = Color(0xFF1D9E75)
    val Warning = Color(0xFFEF9F27)
    val Danger  = Color(0xFFE24B4A)
    val Info    = Color(0xFF378ADD)

    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
}

internal val LightColorScheme = lightColorScheme(
    primary            = PawmingColors.Primary500,
    onPrimary          = PawmingColors.White,
    primaryContainer   = PawmingColors.Primary300,
    onPrimaryContainer = PawmingColors.Primary700,
    secondary          = PawmingColors.Secondary600,
    onSecondary        = PawmingColors.White,
    background         = PawmingColors.Neutral50,
    onBackground       = PawmingColors.Neutral900,
    surface            = PawmingColors.White,
    onSurface          = PawmingColors.Neutral900,
    surfaceVariant     = PawmingColors.Neutral100,
    onSurfaceVariant   = PawmingColors.Neutral700,
    outline            = PawmingColors.Neutral200,
    error              = PawmingColors.Danger,
    onError            = PawmingColors.White
)

internal val DarkColorScheme = darkColorScheme(
    primary            = PawmingColors.Primary400,
    onPrimary          = PawmingColors.Neutral900,
    primaryContainer   = PawmingColors.Primary600,
    onPrimaryContainer = PawmingColors.Primary300,
    secondary          = PawmingColors.Secondary400,
    onSecondary        = PawmingColors.Neutral900,
    background         = PawmingColors.Neutral900,
    onBackground       = PawmingColors.Neutral50,
    surface            = Color(0xFF2A2520),
    onSurface          = PawmingColors.Neutral50,
    surfaceVariant     = Color(0xFF3A3530),
    onSurfaceVariant   = PawmingColors.Neutral200,
    outline            = PawmingColors.Neutral700,
    error              = PawmingColors.Danger,
    onError            = PawmingColors.White
)
