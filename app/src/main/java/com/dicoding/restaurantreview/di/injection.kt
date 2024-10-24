package com.dicoding.restaurantreview.di

import android.content.Context
import com.dicoding.restaurantreview.data.local.room.FavoriteEventDatabase
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import com.dicoding.restaurantreview.data.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventDatabase.getInstance(context)
        val dao = database.favoriteEventDao()
//        val appExecutors = AppExecutors()
        return EventRepository.getInstance(dao, apiService)
    }
}