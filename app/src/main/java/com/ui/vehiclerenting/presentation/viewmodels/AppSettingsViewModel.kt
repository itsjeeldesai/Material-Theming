package com.ui.vehiclerenting.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AppSettingsViewModel : ViewModel() {
    private val _useDynamicColor = mutableStateOf(true)
    val useDynamicColor: State<Boolean> = _useDynamicColor

    fun toggleDynamicColor() {
        _useDynamicColor.value = !_useDynamicColor.value
    }
}