package com.dicoding.restaurantreview.ui.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.restaurantreview.data.response.EventResponse
import com.dicoding.restaurantreview.data.response.ListEventsItem
import com.dicoding.restaurantreview.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>?>()
    val upcomingEvents: LiveData<List<ListEventsItem>?> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>?>()
    val finishedEvents: LiveData<List<ListEventsItem>?> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var loadingCounter = 0

    companion object {
        private const val TAG = "HomeViewModel"
        private const val eventQueryUpcoming = 1
        private const val eventQueryFinished = 0
    }

    init {
        fetchEventDataUpcoming()
        fetchEventDataFinished() // Ensure this is called to fetch finished events
    }

    private fun fetchEventDataUpcoming() {
        _isLoading.value = true
        loadingCounter++
        val client = ApiConfig.getApiService().getEvents(eventQueryUpcoming)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                loadingCounter--
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        Log.d(TAG, "Upcoming events received: ${eventResponse.listEvents}")
                        _upcomingEvents.value = eventResponse.listEvents
                    } else {
                        Log.e(TAG, "Response body is null")
                        _errorMessage.value = "Failed to load upcoming events."
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load upcoming events."
                }
                _isLoading.value = loadingCounter > 0
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                loadingCounter--
                Log.e(TAG, "onFailure: ${t.message}")
                _errorMessage.value = "Failed to load upcoming events."
                _isLoading.value = loadingCounter > 0
            }
        })
    }

    private fun fetchEventDataFinished() {
        _isLoading.value = true
        loadingCounter++
        val client = ApiConfig.getApiService().getEvents(eventQueryFinished)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                loadingCounter--
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        Log.d(TAG, "Finished events received: ${eventResponse.listEvents}")
                        _finishedEvents.value = eventResponse.listEvents
                    } else {
                        Log.e(TAG, "Response body is null")
                        _errorMessage.value = "Failed to load finished events."
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load finished events. Check your internet connection."
                }
                _isLoading.value = loadingCounter > 0
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                loadingCounter--
                Log.e(TAG, "onFailure: ${t.message}")
                _errorMessage.value = "Failed to load finished events. Check your internet connection."
                _isLoading.value = loadingCounter > 0
            }
        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

}