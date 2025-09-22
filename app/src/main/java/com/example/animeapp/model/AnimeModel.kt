package com.example.animeapp.model

data class AnimeResponse(
    val data: List<Anime>
)

data class Anime(
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val images: Images
)

data class Images(
    val jpg: Jpg
)

data class Jpg(
    val image_url: String
)

