package com.example.animeapp.model

data class AnimeCharacterResponse(
    val data: List<CharacterEntry>
)

data class CharacterEntry(
    val character: Character
)

data class Character(
    val name: String
)
