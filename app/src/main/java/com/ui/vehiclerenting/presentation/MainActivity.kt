package com.ui.vehiclerenting.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ui.vehiclerenting.presentation.navigation.CustomNavigationBar
import com.ui.vehiclerenting.presentation.viewmodels.AppSettingsViewModel
import com.ui.vehiclerenting.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ui.vehiclerenting.ui.theme.grayColorScheme
import com.ui.vehiclerenting.ui.theme.useAppColors

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel<AppSettingsViewModel>()
            val useDynamicColor by viewModel.useDynamicColor
            AppTheme(useDynamicColor = useDynamicColor) {
                SetBarColor( useAppColors( grayColors = grayColorScheme().surface, defaultColors = MaterialTheme.colorScheme.surface) )
                CustomNavigationBar()
            }
        }
    }
}

@Composable
private fun SetBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = color
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme(useDynamicColor = true) {
    }
}

