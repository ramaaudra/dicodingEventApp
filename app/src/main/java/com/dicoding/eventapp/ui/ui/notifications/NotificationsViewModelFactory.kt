package com.dicoding.eventapp.ui.ui.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.eventapp.data.EventRepository
import com.dicoding.eventapp.di.Injection

class NotificationsViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: NotificationsViewModelFactory? = null
        fun getInstance(context: Context): NotificationsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: NotificationsViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}