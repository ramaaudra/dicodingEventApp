package com.dicoding.eventapp.ui.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.eventapp.data.EventRepository
import com.dicoding.eventapp.data.local.entity.FavoriteEventEntity
import kotlinx.coroutines.launch
import com.dicoding.eventapp.data.Result

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _bookmarkedEvents = MutableLiveData<Result<List<FavoriteEventEntity>?>>()
    val bookmarkedEvents: LiveData<Result<List<FavoriteEventEntity>?>> = _bookmarkedEvents

    private val _searchResults = MutableLiveData<Result<List<FavoriteEventEntity>?>>()
    val searchResults: LiveData<Result<List<FavoriteEventEntity>?>> = _searchResults

    init {
        fetchFavoriteEvents()
    }

    private fun fetchFavoriteEvents() {
        _bookmarkedEvents.value = Result.Loading
        viewModelScope.launch {
            try {
                eventRepository.getFavoriteEvents().observeForever { events ->
                    if (events == null) {
                        _bookmarkedEvents.value = Result.Error("Error fetching favorite events")
                    } else if (events.isEmpty()) {
                        _bookmarkedEvents.value = Result.Success(emptyList())
                    } else {
                        _bookmarkedEvents.value = Result.Success(events)
                    }
                }
            } catch (e: Exception) {
                _bookmarkedEvents.value = Result.Error("Error fetching favorite events: ${e.message}")
            }
        }
    }

    fun searchFavoriteEvents(query: String) {
        _searchResults.value = Result.Loading
        viewModelScope.launch {
            _searchResults.value = eventRepository.searchFavoriteEvents(query)
        }
    }
}