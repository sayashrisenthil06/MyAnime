package com.example.animeapp.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.animeapp.room.entity.AnimeDetailEntity
import com.example.animeapp.room.entity.CharacterEntity
import androidx.room.Room
import com.example.animeapp.room.dao.AnimeDetaiDAO

@Database(entities = [AnimeDetailEntity::class, CharacterEntity::class], version = 1)
abstract class AnimeDetailDB : RoomDatabase() {
    abstract fun animeDao(): AnimeDetaiDAO
    companion object {
        @Volatile private var INSTANCE: AnimeDetailDB? = null
        fun getInstance(context: Context): AnimeDetailDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDetailDB::class.java,
                    "anime_det_db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
