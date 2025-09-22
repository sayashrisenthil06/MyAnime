package com.example.animeapp.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.animeapp.room.dao.AnimeDao
import com.example.animeapp.room.entity.AnimeEntity

@Database(entities = [AnimeEntity::class], version = 1)
abstract class AnimeDB : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
        @Volatile
        private var INSTANCE: AnimeDB? = null

        fun getDatabase(context: Context): AnimeDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDB::class.java,
                    "anime_db"
                )
                    .allowMainThreadQueries() // Only for non-suspend usage
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}