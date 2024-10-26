package com.dicoding.restaurantreview.ui.ui.setting

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dicoding.restaurantreview.ui.ui.ReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.setDarkMode(isDarkModeActive)
        }
    }

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            preferences.saveReminderSetting(isReminderActive)
        }
    }

    fun getReminderSetting(): LiveData<Boolean> {
        return preferences.getReminderSetting().asLiveData()
    }

    fun scheduleDailyReminder(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val reminderRequest = PeriodicWorkRequest.Builder(
            ReminderWorker::class.java,
            24, TimeUnit.HOURS // Schedule the reminder to run daily
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "DailyReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest
        )
    }

    fun cancelDailyReminder(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork("DailyReminder")
    }
}