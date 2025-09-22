package com.example.animeapp.room

import com.example.animeapp.model.Anime
import com.example.animeapp.room.entity.AnimeEntity

fun List<Anime>.toEntityList(): List<AnimeEntity> {
    return this.map {
        AnimeEntity(
            mal_id = it.mal_id,
            title = it.title,
            episodes = it.episodes,
            score = it.score,
            image_url = it.images.jpg.image_url
        )
    }
}
