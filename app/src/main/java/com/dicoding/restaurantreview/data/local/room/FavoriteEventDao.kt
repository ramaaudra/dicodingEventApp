package com.dicoding.restaurantreview.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.restaurantreview.data.local.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_event")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_event WHERE id = :id)")
    suspend fun isEventFavorited(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(event: List<FavoriteEventEntity>)

    @Update
    suspend fun updateNews(event: FavoriteEventEntity)

    @Delete
    suspend fun deleteAll(event: FavoriteEventEntity)

    @Query("SELECT * FROM favorite_event WHERE id = :eventId")
    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEventEntity?>

    @Query("SELECT * FROM favorite_event WHERE name LIKE '%' || :query || '%'")
    suspend fun searchFavoriteEvents(query: String): List<FavoriteEventEntity>
}