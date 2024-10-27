package com.dicoding.eventapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.eventapp.data.local.entity.FavoriteEventEntity

@Database(entities = [FavoriteEventEntity::class], version = 2, exportSchema = false)
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Rename the old table
                db.execSQL("ALTER TABLE favorite_event RENAME TO FavoriteEventEntity_old")

                // Create the new table with the updated schema
                db.execSQL(
                    """
            CREATE TABLE FavoriteEventEntity (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                mediaCover TEXT
            )
        """.trimIndent()
                )

                // Copy the data from the old table to the new table
                db.execSQL(
                    """
            INSERT INTO FavoriteEventEntity (id, name, mediaCover)
            SELECT id, name, mediaCover FROM FavoriteEventEntity_old
        """.trimIndent()
                )

                // Drop the old table
                db.execSQL("DROP TABLE FavoriteEventEntity_old")
            }
        }
    }
}