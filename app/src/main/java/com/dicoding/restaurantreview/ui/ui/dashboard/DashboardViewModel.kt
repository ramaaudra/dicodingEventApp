package com.dicoding.restaurantreview.ui.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.EventRepository
import com.dicoding.restaurantreview.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch
import com.dicoding.restaurantreview.data.Result

class DashboardViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<Result<List<ListEventsItem>?>>()
    val upcomingEvents: LiveData<Result<List<ListEventsItem>?>> = _upcomingEvents

    private val _searchResults = MutableLiveData<Result<List<ListEventsItem>?>>()
    val searchResults: LiveData<Result<List<ListEventsItem>?>> = _searchResults

    init {
        fetchEventDataUpcoming()
    }

    private fun fetchEventDataUpcoming() {
        _upcomingEvents.value = Result.Loading
        viewModelScope.launch {
            _upcomingEvents.value = eventRepository.fetchUpcomingEvents()
        }
    }

    fun searchUpcomingEvents(query: String) {
        _searchResults.value = Result.Loading
        viewModelScope.launch {
            _searchResults.value = eventRepository.searchUpcomingEvents(query)
        }
    }
}
