package com.example.animeapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animeapp.room.entity.AnimeEntity

// AnimeDao.kt
@Dao
interface AnimeDao {

    @Query("SELECT * FROM anime_table")
    fun getAllAnime(): List<AnimeEntity> // non-suspend

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnime(animeList: List<AnimeEntity>)

    @Query("DELETE FROM anime_table")
    fun clearAnime()
}


