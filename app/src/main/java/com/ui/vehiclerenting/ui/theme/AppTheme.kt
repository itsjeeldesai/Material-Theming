package com.ui.vehiclerenting.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

data class AppTheme(
    val mainColorScheme: ColorScheme,
    val grayColorScheme: ColorScheme,
    val isDynamicColorEnabled: Boolean
)

val LocalAppTheme = compositionLocalOf<AppTheme> { error("AppTheme not provided") }

/*
@Composable
fun useAppColors(): ColorScheme {
    val appTheme = LocalAppTheme.current
    return if (appTheme.isDynamicColorEnabled) {
        appTheme.mainColorScheme
    } else {
        appTheme.grayColorScheme
    }
}
*/

/*val ColorScheme.blueGray: ColorScheme // to access the colorscheme like : MaterialTheme.colorscheme.blueGray...
    @Composable
    get() = LocalAppTheme.current.grayColorScheme*/

@Composable
fun grayColorScheme(): ColorScheme = LocalAppTheme.current.grayColorScheme

@Composable
fun <T> useAppColors(grayColors: T, defaultColors: T): T {
    val appTheme = LocalAppTheme.current
    return if (appTheme.isDynamicColorEnabled) {
        defaultColors
    } else {
        grayColors
    }
}