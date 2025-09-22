package com.example.animeapp.model

data class AnimeDetailResponse(
    val data: AnimeDetail
)

data class AnimeDetail(
    val mal_id: Int,
    val title: String,
    val title_english: String?,
    val title_japanese: String?,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val trailer: Trailer?,
    val images: Images,
    val genres: List<Genre>,
    val rating: String?,
    val duration: String?,
    val background: String?
)

data class Trailer(
    val youtube_id: String?,
    val url: String?,
    val embed_url: String?
)

data class ImageJpg(
    val image_url: String
)

data class Genre(
    val name: String
)
