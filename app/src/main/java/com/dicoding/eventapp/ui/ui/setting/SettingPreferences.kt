package com.dicoding.eventapp.ui.ui.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingPreferences(private val dataStore: DataStore<Preferences>) {

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    private val REMINDERKEY = booleanPreferencesKey("daily_reminder")
    companion object {

        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                preferences[DARK_MODE_KEY] ?: false // default to false (Light Mode)
            }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun getThemeSettingSync(): Boolean {
        return dataStore.data
            .map { preferences ->
                preferences[DARK_MODE_KEY] ?: false // default ke false jika tidak ada pengaturan
            }
            .first() // mengambil data pertama (sinkron)
    }

    fun getReminderSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[REMINDERKEY] ?: false
        }
    }

    suspend fun saveReminderSetting(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMINDERKEY] = enabled
        }
    }
}