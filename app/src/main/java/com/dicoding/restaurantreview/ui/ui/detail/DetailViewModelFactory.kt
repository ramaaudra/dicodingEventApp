package com.dicoding.restaurantreview.ui.ui.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.restaurantreview.data.EventRepository
import com.dicoding.restaurantreview.di.Injection

class DetailViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null
        fun getInstance(context: Context): DetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}