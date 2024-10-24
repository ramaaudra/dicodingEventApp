package com.dicoding.restaurantreview.ui.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.remote.response.EventResponse
import com.dicoding.restaurantreview.data.remote.response.ListEventsItem
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.dicoding.restaurantreview.data.Result


class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<Result<List<ListEventsItem>?>>()
    val upcomingEvents: LiveData<Result<List<ListEventsItem>?>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<Result<List<ListEventsItem>?>>()
    val finishedEvents: LiveData<Result<List<ListEventsItem>?>> = _finishedEvents


    companion object {
        private const val TAG = "HomeViewModel"
        private const val eventQueryUpcoming = 1
        private const val eventQueryFinished = 0
    }

    init {
        fetchEventDataUpcoming()
        fetchEventDataFinished()
    }

    private fun fetchEventDataUpcoming() {
        _upcomingEvents.value = Result.Loading
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(eventQueryUpcoming)
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        _upcomingEvents.value = Result.Success(eventResponse.listEvents)
                    } else {
                        _upcomingEvents.value = Result.Error("Failed to load upcoming events. Check your internet connection.")
                    }
                } else {
                    _upcomingEvents.value = Result.Error("Failed to load upcoming events. Check your internet connection.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                _upcomingEvents.value = Result.Error("Failed to load upcoming events. Check your internet connection.")
            }
        }
    }

    private fun fetchEventDataFinished() {
        _finishedEvents.value = Result.Loading
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(eventQueryFinished)
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        _finishedEvents.value = Result.Success(eventResponse.listEvents)
                    } else {
                        _finishedEvents.value = Result.Error("Failed to load finished events. Check your internet connection.")
                    }
                } else {
                    _finishedEvents.value = Result.Error("Failed to load finished events. Check your internet connection.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                _finishedEvents.value = Result.Error("Failed to load finished events. Check your internet connection.")
            }
        }
    }

}