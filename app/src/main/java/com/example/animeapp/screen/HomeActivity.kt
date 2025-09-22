package com.example.animeapp.screen

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeapp.R
import com.example.animeapp.adapter.AnimeAdapter
import com.example.animeapp.model.Anime
import com.example.animeapp.model.AnimeResponse
import com.example.animeapp.model.Images
import com.example.animeapp.model.Jpg
import com.example.animeapp.retrofit.ApiClient
import com.example.animeapp.room.AnimeDB
import com.example.animeapp.room.AnimeRepository
import com.example.animeapp.utils.NetworkCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var animeAdapter: AnimeAdapter
    private lateinit var repo: AnimeRepository
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: NetworkCallback


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val dao = AnimeDB.getDatabase(this).animeDao()
        repo = AnimeRepository(dao)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = NetworkCallback(this) { isOnline ->
            runOnUiThread {
                if (isOnline) {
                    Toast.makeText(this, "You are Online", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "You are Offline", Toast.LENGTH_SHORT).show()
                }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        fetchTopAnime()

    }
    private fun fetchTopAnime() {
        repo.getAnime { animeList ->
            val animeListFromEntity: List<Anime> = animeList.map {
                Anime(
                    mal_id = it.mal_id,
                    title = it.title,
                    episodes = it.episodes,
                    score = it.score,
                    images = Images(Jpg(it.image_url))
                )
            }
            animeAdapter = AnimeAdapter(animeListFromEntity)
            recyclerView.adapter = animeAdapter
            animeAdapter.notifyDataSetChanged()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
