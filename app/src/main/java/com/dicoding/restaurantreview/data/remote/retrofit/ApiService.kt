package com.dicoding.restaurantreview.data.remote.retrofit

import com.dicoding.restaurantreview.data.remote.response.DetailEventResponse
import com.dicoding.restaurantreview.data.remote.response.EventResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int
    ): Response<EventResponse>

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: String
    ): Response<DetailEventResponse>

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int,
        @Query("q") query: String
    ): Response<EventResponse>

//    @GET("events")
//    suspend fun getActiveEvent(
//        @Query("active") active: Int = -1,
//        @Query("limit") limit: Int = 1
//    ): Map<String, Any>
}