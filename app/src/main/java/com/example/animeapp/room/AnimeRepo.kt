package com.example.animeapp.room

import com.example.animeapp.model.AnimeResponse
import com.example.animeapp.retrofit.ApiClient
import com.example.animeapp.room.dao.AnimeDao
import com.example.animeapp.room.entity.AnimeEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimeRepository(private val dao: AnimeDao) {

    fun getAnime(callback: (List<AnimeEntity>) -> Unit) {
        // Load offline data first
        val offlineData = dao.getAllAnime()
        if (offlineData.isNotEmpty()) {
            callback(offlineData)
        }

        // Fetch online and update DB
        ApiClient.apiService.getTopAnime().enqueue(object : Callback<AnimeResponse> {
            override fun onResponse(call: Call<AnimeResponse>, response: Response<AnimeResponse>) {
                if (response.isSuccessful) {
                    val animeList = response.body()?.data ?: emptyList()
                    val entityList = animeList.toEntityList()
                    dao.clearAnime()
                    dao.insertAnime(entityList)
                    callback(entityList)
                }
            }

            override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
            }
        })

    }
}

