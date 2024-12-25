package com.ui.vehiclerenting.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark
)

val CustomLightColorScheme = lightColorScheme(
    primary = blueLightColor,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E2FF),
    onPrimaryContainer = blueDarkColor,
    secondary = blueDarkColor,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6E2FF),
    onSecondaryContainer = Color(0xFF0F1D3E),
    tertiary = Color(0xFF715573),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFBD7FC),
    onTertiaryContainer = Color(0xFF29132D),
    error = Color(0xFFEA0011),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),
    background = backgroundColor,
    onBackground = Color(0xFF1A1C1E),
    surface = surfaceColor,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44474E),
    outline = Color(0xFF74777F),
    inverseOnSurface = Color(0xFFF1F0F4),
    inverseSurface = Color(0xFF2F3033),
    inversePrimary = Color(0xFFABC7FF),
    surfaceTint = blueLightColor,
    outlineVariant = Color(0xFFC4C6CF),
    scrim = golden,
)

// Dark Theme ColorScheme
val CustomDarkColorScheme = darkColorScheme(
    primary = invertedBlueLightColor,
    onPrimary = Color.Black,
    primaryContainer = invertedBlueDarkColor,
    onPrimaryContainer = Color(0xFF001B3D),
    secondary = invertedBlueDarkColor,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF354A6E),
    onSecondaryContainer = Color(0xFFD6E2FF),
    tertiary = Color(0xFFDEBCDF),
    onTertiary = Color(0xFF402843),
    tertiaryContainer = Color(0xFF583E5B),
    onTertiaryContainer = Color(0xFFFBD7FC),
    error = Color(0xFFEA0011),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = invertedBackgroundColor,
    onBackground = Color(0xFFE3E2E6),
    surface = invertedSurfaceColor,
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF44474E),
    onSurfaceVariant = Color(0xFFC4C6CF),
    outline = Color(0xFF8E9099),
    inverseOnSurface = Color(0xFF1A1C1E),
    inverseSurface = Color(0xFFE3E2E6),
    inversePrimary = blueDarkColor,
    surfaceTint = invertedBlueLightColor,
    outlineVariant = Color(0xFF44474E),
    scrim = invertedGolden
)


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val dynamicColor = useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme = when {
        dynamicColor && useDarkTheme -> dynamicDarkColorScheme(context)
        dynamicColor && !useDarkTheme -> dynamicLightColorScheme(context)
        useDarkTheme -> darkScheme
        else -> lightScheme
    }

    val grayColorScheme = if (useDarkTheme) CustomDarkColorScheme else CustomLightColorScheme

    val appTheme = AppTheme(
        mainColorScheme = colorScheme,
        grayColorScheme = grayColorScheme,
        isDynamicColorEnabled = dynamicColor
    )

    CompositionLocalProvider(
        LocalAppTheme provides appTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
