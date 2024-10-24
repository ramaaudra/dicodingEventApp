package com.dicoding.restaurantreview.ui.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.remote.response.EventResponse
import com.dicoding.restaurantreview.data.remote.response.ListEventsItem
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class NotificationsViewModel : ViewModel() {

    private val _event = MutableLiveData<List<ListEventsItem>?>()
    val event: LiveData<List<ListEventsItem>?> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchResults = MutableLiveData<List<ListEventsItem>?>()
    val searchResults: LiveData<List<ListEventsItem>?> = _searchResults

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object {
        private const val TAG = "NotificationsViewModel"
        private const val eventQuery = 0
    }

    init {
        fetchEventData()
    }

    private fun fetchEventData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(eventQuery)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        Log.d(TAG, "Data received: ${eventResponse.listEvents}")
                        _event.value = eventResponse.listEvents
                    } else {
                        Log.e(TAG, "Response body is null")
                        _errorMessage.value = "Failed to load events. Check your internet connection."
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load events. Check your internet connection."
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "Exception: ${e.message}")
                _errorMessage.value = "Failed to load events. Check your internet connection."
            }
        }
    }

    fun searchFinishedEvents(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().searchEvents(eventQuery, query)
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to search events. Check your internet connection."
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "Exception: ${e.message}")
                _errorMessage.value = "Failed to search events. Check your internet connection."
            }
        }
    }
}