package com.dicoding.eventapp.ui.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.eventapp.data.EventRepository
import com.dicoding.eventapp.di.Injection


class DashboardViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: DashboardViewModelFactory? = null
        fun getInstance(context: Context): DashboardViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DashboardViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}