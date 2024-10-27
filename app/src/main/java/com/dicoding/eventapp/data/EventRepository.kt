package com.dicoding.eventapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.dicoding.eventapp.data.local.entity.FavoriteEventEntity
import com.dicoding.eventapp.data.local.room.FavoriteEventDao
import com.dicoding.eventapp.data.remote.response.Event
import com.dicoding.eventapp.data.remote.response.EventResponse
import com.dicoding.eventapp.data.remote.response.ListEventsItem
import com.dicoding.eventapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlinx.coroutines.flow.first


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

    suspend fun isEventFavorited(id: Int): Boolean {
        return favoriteEventDao.isEventFavorited(id)
    }

    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEventEntity?> {
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

    suspend fun fetchUpcomingEvents(): Result<List<ListEventsItem>?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response: Response<EventResponse> = apiService.getEvents(1)
            if (response.isSuccessful) {
                val eventResponse = response.body()
                Result.Success(eventResponse?.listEvents)
            } else {
                Result.Error("Failed to load upcoming events: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    suspend fun fetchFinishedEvents(): Result<List<ListEventsItem>?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response: Response<EventResponse> = apiService.getEvents(0)
            if (response.isSuccessful) {
                val eventResponse = response.body()
                Result.Success(eventResponse?.listEvents)
            } else {
                Result.Error("Failed to load finished events: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message},please try again later")
        }
    }

    suspend fun searchUpcomingEvents(query: String): Result<List<ListEventsItem>?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response: Response<EventResponse> = apiService.searchEvents(1, query)
            if (response.isSuccessful) {
                Result.Success(response.body()?.listEvents)
            } else {
                Result.Error("Failed to search upcoming events: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    suspend fun searchFinishedEvents(query: String): Result<List<ListEventsItem>?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response: Response<EventResponse> = apiService.searchEvents(0, query)
            if (response.isSuccessful) {
                Result.Success(response.body()?.listEvents)
            } else {
                Result.Error("Failed to search finished events: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}, please try again later")
        }
    }

    suspend fun searchFavoriteEvents(query: String): Result<List<FavoriteEventEntity>?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val favoriteEvents = favoriteEventDao.searchFavoriteEvents(query)
            if (favoriteEvents.isNotEmpty()) {
                Result.Success(favoriteEvents)
            } else {
                Result.Error("No results found for \"$query\"")
            }
        } catch (e: Exception) {
            Result.Error("Error searching favorite events: ${e.localizedMessage}")
        }
    }

    fun getFavoriteEvents(): LiveData<List<FavoriteEventEntity>> {
        return favoriteEventDao.getAllFavoriteEvents()
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