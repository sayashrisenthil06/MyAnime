package com.example.animeapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animeapp.room.entity.AnimeDetailEntity
import com.example.animeapp.room.entity.CharacterEntity

@Dao
interface AnimeDetaiDAO {
    // Anime
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnime(anime: AnimeDetailEntity)

    @Query("SELECT * FROM anime_detail_table WHERE malId = :id")
    fun getAnimeById(id: Int): AnimeDetailEntity?

    // Characters
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM character_table WHERE animeId = :animeId")
    fun getCharactersForAnime(animeId: Int): List<CharacterEntity>
}
