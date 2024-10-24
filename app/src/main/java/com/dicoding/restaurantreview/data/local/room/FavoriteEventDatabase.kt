package com.dicoding.restaurantreview.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.restaurantreview.data.local.entity.FavoriteEventEntity

@Database(entities = [FavoriteEventEntity::class], version = 1, exportSchema = false)
abstract class FavoriteEventDatabase : RoomDatabase() {

    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteEventDatabase? = null

        fun getInstance(context: Context): FavoriteEventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteEventDatabase::class.java,
                    "favorite_event_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}