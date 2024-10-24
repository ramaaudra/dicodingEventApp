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
import com.dicoding.restaurantreview.data.Result

class NotificationsViewModel : ViewModel() {

    private val _event = MutableLiveData<Result<List<ListEventsItem>?>>()
    val event: LiveData<Result<List<ListEventsItem>?>> = _event

    private val _searchResults = MutableLiveData<Result<List<ListEventsItem>?>>()
    val searchResults: LiveData<Result<List<ListEventsItem>?>> = _searchResults

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
        _event.value = Result.Loading
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(eventQuery)
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        Log.d(TAG, "Data received: ${eventResponse.listEvents}")
                        _event.value = Result.Success(eventResponse.listEvents)
                    } else {
                        Log.e(TAG, "Response body is null")
                        _event.value = Result.Error("Failed to load events. Check your internet connection.")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _event.value = Result.Error("Failed to load events. Check your internet connection.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                _event.value = Result.Error("Failed to load events. Check your internet connection.")
            }
        }
    }

    fun searchFinishedEvents(query: String) {
        _searchResults.value = Result.Loading
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().searchEvents(eventQuery, query)
                if (response.isSuccessful) {
                    _searchResults.value = Result.Success(response.body()?.listEvents)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _searchResults.value = Result.Error("Failed to search events. Check your internet connection.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                _searchResults.value = Result.Error("Failed to search events. Check your internet connection.")
            }
        }
    }
}