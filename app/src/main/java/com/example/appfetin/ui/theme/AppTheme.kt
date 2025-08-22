package com.example.appfetin.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBrown,
    onPrimary = WarmGray50,
    primaryContainer = PrimaryBrownLight,
    onPrimaryContainer = WarmGray50,
    secondary = PrimaryBrownSoft,
    onSecondary = WarmGray50,
    secondaryContainer = WarmGray700,
    onSecondaryContainer = WarmGray200,
    tertiary = AccentBlue,
    onTertiary = WarmGray50,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    error = ErrorRed,
    onError = WarmGray50
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBrown,
    onPrimary = WarmGray50,
    primaryContainer = PrimaryBrownSoft,
    onPrimaryContainer = WarmGray900,
    secondary = PrimaryBrownLight,
    onSecondary = WarmGray50,
    secondaryContainer = WarmGray100,
    onSecondaryContainer = WarmGray800,
    tertiary = AccentBlue,
    onTertiary = WarmGray50,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    error = ErrorRed,
    onError = WarmGray50
)

@Composable
fun AppFetinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
