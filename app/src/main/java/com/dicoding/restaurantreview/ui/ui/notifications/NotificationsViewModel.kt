package com.dicoding.restaurantreview.ui.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.EventRepository
import com.dicoding.restaurantreview.data.remote.response.EventResponse
import com.dicoding.restaurantreview.data.remote.response.ListEventsItem
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response
import com.dicoding.restaurantreview.data.Result

class NotificationsViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _event = MutableLiveData<Result<List<ListEventsItem>?>>()
    val event: LiveData<Result<List<ListEventsItem>?>> = _event

    private val _searchResults = MutableLiveData<Result<List<ListEventsItem>?>>()
    val searchResults: LiveData<Result<List<ListEventsItem>?>> = _searchResults

    init {
        fetchEventData()
    }

    private fun fetchEventData() {
        _event.value = Result.Loading
        viewModelScope.launch {
            _event.value = eventRepository.fetchFinishedEvents()
        }
    }

    fun searchFinishedEvents(query: String) {
        _searchResults.value = Result.Loading
        viewModelScope.launch {
            _searchResults.value = eventRepository.searchFinishedEvents(query)

        }
    }
}
