package com.dicoding.restaurantreview.data

import androidx.lifecycle.LiveData
import com.dicoding.restaurantreview.data.local.entity.FavoriteEventEntity
import com.dicoding.restaurantreview.data.local.room.FavoriteEventDao
import com.dicoding.restaurantreview.data.remote.response.Event
import com.dicoding.restaurantreview.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(
    private val favoriteEventDao: FavoriteEventDao,
    private val apiService: ApiService
) {


    suspend fun insertFavoriteEvent(event: FavoriteEventEntity) {
        favoriteEventDao.insertNews(listOf(event))
    }

    suspend fun deleteFavoriteEvent(event: FavoriteEventEntity) {
        favoriteEventDao.deleteAll(event)
    }

    suspend fun isEventFavorited(id: String): Boolean {
        return favoriteEventDao.isEventFavorited(id)
    }

    fun getFavoriteEventById(eventId: String): LiveData<FavoriteEventEntity?> {
        return favoriteEventDao.getFavoriteEventById(eventId)
    }

    suspend fun fetchEventDetail(eventId: Int): Result<Event?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.getDetailEvent(eventId.toString())
            if (response.isSuccessful) {
                val eventResponse = response.body()
                Result.Success(eventResponse?.event)
            } else {
                Result.Error("Failed to load data: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            dao: FavoriteEventDao,
            apiService: ApiService
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(dao, apiService).also { instance = it }
            }
    }
}