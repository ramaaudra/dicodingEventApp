package com.dicoding.restaurantreview.ui.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.EventRepository
import com.dicoding.restaurantreview.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch
import com.dicoding.restaurantreview.data.Result



class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<Result<List<ListEventsItem>?>>()
    val upcomingEvents: LiveData<Result<List<ListEventsItem>?>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<Result<List<ListEventsItem>?>>()
    val finishedEvents: LiveData<Result<List<ListEventsItem>?>> = _finishedEvents

    init {
        fetchEventDataUpcoming()
        fetchEventDataFinished()
    }

    private fun fetchEventDataUpcoming() {
        _upcomingEvents.value = Result.Loading
        viewModelScope.launch {
            _upcomingEvents.value = eventRepository.fetchUpcomingEvents()
        }
    }

    private fun fetchEventDataFinished() {
        _finishedEvents.value = Result.Loading
        viewModelScope.launch {
            _finishedEvents.value = eventRepository.fetchFinishedEvents()
        }
    }
}