package com.example.animeapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val image_url: String
)