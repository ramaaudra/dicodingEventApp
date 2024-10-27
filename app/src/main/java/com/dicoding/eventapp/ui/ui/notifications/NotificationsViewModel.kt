package com.dicoding.eventapp.ui.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.eventapp.data.EventRepository
import com.dicoding.eventapp.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch
import com.dicoding.eventapp.data.Result

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
