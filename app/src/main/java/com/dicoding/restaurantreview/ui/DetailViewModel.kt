package com.dicoding.restaurantreview.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.EventRepository
import com.dicoding.restaurantreview.data.local.entity.FavoriteEventEntity
import com.dicoding.restaurantreview.data.remote.response.Event

import kotlinx.coroutines.launch
import com.dicoding.restaurantreview.data.Result

    class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
        private val _eventDetail = MutableLiveData<Event?>()
        val eventDetail: LiveData<Event?> = _eventDetail

        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> = _isLoading

        private val _errorMessage = MutableLiveData<String?>()
        val errorMessage: LiveData<String?> = _errorMessage

        companion object {
            private const val TAG = "DetailViewModel"
        }


        fun fetchEventDetail(eventId: Int) {
            _isLoading.value = true
            viewModelScope.launch {
                val result = eventRepository.fetchEventDetail(eventId)
                _isLoading.value = false
                when (result) {
                    is Result.Success -> _eventDetail.value = result.data
                    is Result.Error -> _errorMessage.value = result.message
                    else -> _errorMessage.value = "Unknown error"
                }
            }
        }

                fun insertFavoriteEvent(event: FavoriteEventEntity) {
            viewModelScope.launch {
                eventRepository.insertFavoriteEvent(event)
            }
        }
    }