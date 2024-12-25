package com.ui.vehiclerenting.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_THEME] ?: false
    }

    val useDynamicColor: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: true
    }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = isDarkTheme
        }
    }

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = useDynamicColor
        }
    }
}