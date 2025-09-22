package com.example.animeapp.room.entity

import androidx.room.*

@Entity(tableName = "anime_detail_table")
data class AnimeDetailEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val rating: String?,
    val duration: String?,
    val background: String?,
    val imageUrl: String,          // Flattened from Images.jpg.image_url
    val trailerUrl: String?,       // Flattened from Trailer.embed_url
    val genres: String             // Comma-separated genres
)

@Entity(
    tableName = "character_table",
    foreignKeys = [ForeignKey(
        entity = AnimeDetailEntity::class,
        parentColumns = ["malId"],
        childColumns = ["animeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("animeId")]
)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val animeId: Int,   // FK to AnimeEntity
    val name: String
)
