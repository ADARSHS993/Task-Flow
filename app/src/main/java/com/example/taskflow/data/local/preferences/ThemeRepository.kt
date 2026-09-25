package com.example.taskflow.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.themeDataStore by preferencesDataStore(
    name = "theme_preferences"
)

@Singleton
class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val darkModeKey = booleanPreferencesKey("dark_mode")

    val isDarkMode: Flow<Boolean> =
        context.themeDataStore.data.map { preferences ->
            preferences[darkModeKey] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[darkModeKey] = enabled
        }
    }
}