package com.dicoding.restaurantreview.ui.ui.dashboard

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

class DashboardViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>?>()
    val upcomingEvents: LiveData<List<ListEventsItem>?> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>?>()
    val finishedEvents: LiveData<List<ListEventsItem>?> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _searchResults = MutableLiveData<List<ListEventsItem>?>()
    val searchResults: LiveData<List<ListEventsItem>?> = _searchResults

    companion object {
        private const val TAG = "DashboardViewModel"
        private const val eventQueryUpcoming = 1
        private const val eventQueryFinished = 0
    }

    init {
        fetchEventDataUpcoming()
        fetchEventDataFinished()
    }

    private fun fetchEventDataUpcoming() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(eventQueryUpcoming)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        Log.d(TAG, "Upcoming events received: ${eventResponse.listEvents}")
                        _upcomingEvents.value = eventResponse.listEvents
                    } else {
                        Log.e(TAG, "Response body is null")
                        _errorMessage.value = "Failed to load upcoming events. Check your internet connection"
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load upcoming events. Check your internet connection"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "Exception: ${e.message}")
                _errorMessage.value = "Failed to load upcoming events. Check your internet connection."
            }
        }
    }

    private fun fetchEventDataFinished() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(eventQueryFinished)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        Log.d(TAG, "Finished events received: ${eventResponse.listEvents}")
                        _finishedEvents.value = eventResponse.listEvents
                    } else {
                        Log.e(TAG, "Response body is null")
                        _errorMessage.value = "Failed to load finished events. Check your internet connection."
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load finished events. Check your internet connection."
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "Exception: ${e.message}")
                _errorMessage.value = "Failed to load finished events. Check your internet connection."
            }
        }
    }

    fun searchUpcomingEvents(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().searchEvents(eventQueryUpcoming, query)
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to search upcoming events. Check your internet connection."
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "Exception: ${e.message}")
                _errorMessage.value = "Failed to search upcoming events. Check your internet connection."
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}