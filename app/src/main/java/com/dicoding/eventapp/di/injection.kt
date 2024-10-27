package com.dicoding.eventapp.di

import android.content.Context
import com.dicoding.eventapp.data.local.room.FavoriteEventDatabase
import com.dicoding.eventapp.data.remote.retrofit.ApiConfig
import com.dicoding.eventapp.data.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventDatabase.getInstance(context)
        val dao = database.favoriteEventDao()
//        val appExecutors = AppExecutors()
        return EventRepository.getInstance(dao, apiService)
    }
}