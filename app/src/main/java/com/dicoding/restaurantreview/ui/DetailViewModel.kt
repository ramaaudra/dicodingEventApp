package com.dicoding.restaurantreview.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.EventRepository
import com.dicoding.restaurantreview.data.local.entity.FavoriteEventEntity
import com.dicoding.restaurantreview.data.remote.response.Event

import kotlinx.coroutines.launch
import com.dicoding.restaurantreview.data.Result

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _eventDetail = MutableLiveData<Result<Event?>>()
    val eventDetail: LiveData<Result<Event?>> = _eventDetail

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchEventDetail(eventId: Int) {
        _eventDetail.value = Result.Loading
        viewModelScope.launch {
            val result = eventRepository.fetchEventDetail(eventId)
            _eventDetail.value = result
        }
    }

    fun getEventById(eventId: String): LiveData<FavoriteEventEntity?> {
        return eventRepository.getFavoriteEventById(eventId)
    }

    fun toggleFavoriteEvent(event: Event) {
        viewModelScope.launch {
            val isFavorited = eventRepository.isEventFavorited(event.id.toString())
            if (isFavorited) {
                eventRepository.deleteFavoriteEvent(FavoriteEventEntity(event.id.toString(), event.name, event.mediaCover))
            } else {
                eventRepository.insertFavoriteEvent(FavoriteEventEntity(event.id.toString(), event.name, event.mediaCover))
            }
        }
    }
}