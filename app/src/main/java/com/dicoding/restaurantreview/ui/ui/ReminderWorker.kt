package com.dicoding.restaurantreview.ui.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.booleanPreferencesKey

import androidx.datastore.preferences.preferencesDataStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import com.dicoding.restaurantreview.ui.MainActivity
import com.dicoding.restaurantreview.ui.ui.setting.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extension untuk membuat

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    // Definisikan key untuk DataStore
    private val REMINDER_KEY = booleanPreferencesKey("daily_reminder")

    override suspend fun doWork(): Result {
        Log.d("ReminderWorker", "Worker started")

        // Mengambil status pengingat dari DataStore
        val isReminderActive = context.dataStore.data
            .map { preferences -> preferences[REMINDER_KEY] ?: false }
            .first() // Mengambil nilai pertama dari flow

        Log.d("ReminderWorker", "isReminderActive: $isReminderActive")

        if (isReminderActive) {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getEventReminder()
                Log.d("ReminderWorker", "API Response: $response")

                if (!response.error && response.listEvents.isNotEmpty()) {
                    val event = response.listEvents.first()
                    Log.d("ReminderWorker", "Event Found: ${event.name}")
                    showNotification(event.name, event.beginTime)
                } else {
                    Log.d("ReminderWorker", "No events found or response error")
                }

            } catch (e: Exception) {
                Log.e("ReminderWorker", "Exception occurred: ${e.message}")
                return Result.retry()
            }
        } else {
            Log.d("ReminderWorker", "Reminder is not active")
        }

        return Result.success()
    }

    private fun showNotification(eventName: String, eventTime: String) {
        val channelId = "reminder_channel"
        val channelName = "Reminder Channel"
        val notificationId = 1

        // Buat intent untuk membuka MainActivity saat notifikasi diklik
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // Buat objek NotificationCompat.Builder
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle("Event Terdekat")
            .setContentText("Event: $eventName pada $eventTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Event: $eventName pada $eventTime"))

        // Buat NotificationChannel untuk Android O ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelName
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notify(notificationId, builder.build())
                    Log.d("ReminderWorker", "Notification posted")
                } else {
                    Log.d("ReminderWorker", "Notification permission not granted")
                }
            } else {
                notify(notificationId, builder.build())
                Log.d("ReminderWorker", "Notification posted")
            }
        }
    }
}