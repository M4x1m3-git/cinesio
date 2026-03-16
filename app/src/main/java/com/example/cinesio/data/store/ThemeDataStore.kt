package com.example.cinesio.data.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

object ThemeDataStore {

    private val DARK_THEME = booleanPreferencesKey("dark_theme")

    fun getTheme(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[DARK_THEME] ?: false
        }

    suspend fun saveTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_THEME] = isDark
        }
    }
}