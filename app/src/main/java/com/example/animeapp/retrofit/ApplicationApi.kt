package com.example.animeapp.retrofit

import com.example.animeapp.model.AnimeCharacterResponse
import com.example.animeapp.model.AnimeDetailResponse
import com.example.animeapp.model.AnimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApplicationApi {
    @GET("v4/top/anime")
    fun getTopAnime(): Call<AnimeResponse>
    @GET("v4/anime/{mal_id}")
    fun getAnimeDetail(@Path("mal_id") malId: Int): Call<AnimeDetailResponse>
    @GET("v4/anime/{id}/characters")
    fun getAnimeCharacters(@Path("id") id: Int): Call<AnimeCharacterResponse>
}
