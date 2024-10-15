package com.dicoding.restaurantreview.ui.ui.notifications

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

class NotificationsViewModel : ViewModel() {

    private val _event = MutableLiveData<List<ListEventsItem>?>()
    val event: LiveData<List<ListEventsItem>?> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "NotificationsViewModel"
        private const val eventQuery = 0

    }

    init {
        fetchEventData()
    }

    private fun fetchEventData() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(eventQuery)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val eventResponse = response.body()
                    if (eventResponse != null) {
                        Log.d(TAG, "Data received: ${eventResponse.listEvents}")
                        _event.value = response.body()?.listEvents
                    } else {
                        Log.e(TAG, "Response body is null")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}