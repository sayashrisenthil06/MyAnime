package com.example.animeapp.screen

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.animeapp.R
import com.example.animeapp.model.AnimeCharacterResponse
import com.example.animeapp.model.AnimeDetail
import com.example.animeapp.model.AnimeDetailResponse
import com.example.animeapp.retrofit.ApiClient
import com.example.animeapp.room.dao.AnimeDetaiDAO
import com.example.animeapp.room.db.AnimeDetailDB
import com.example.animeapp.room.entity.AnimeDetailEntity
import com.example.animeapp.room.entity.CharacterEntity
import com.example.animeapp.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimeDetailActivity : AppCompatActivity() {

    // Declare your views here
    private lateinit var posterImage: ImageView
    private lateinit var titleText: TextView
    private lateinit var synopsisText: TextView
    private lateinit var genresText: TextView
    private lateinit var castText: TextView
    private lateinit var episodesText: TextView
    private lateinit var ratingText: TextView
    private lateinit var youtubeWebView: WebView
    private lateinit var animeDao: AnimeDetaiDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Anime Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        animeDao = AnimeDetailDB.getInstance(this).animeDao()
        posterImage = findViewById(R.id.posterImage)
        titleText = findViewById(R.id.title)
        synopsisText = findViewById(R.id.synopsis)
        genresText = findViewById(R.id.genres)
        castText = findViewById(R.id.cast)
        episodesText = findViewById(R.id.episodes)
        ratingText = findViewById(R.id.rating)
        youtubeWebView = findViewById(R.id.youtubeWebView)
        val animeId = intent.getIntExtra("anime_id", -1)
        if (NetworkUtils.isOnline(this)) {
            fetchAnimeDetails(animeId) // API call, then save to DB
            fetchAnimeCharacters(animeId) // API call, then save to DB
        } else {
            loadFromDb(animeId) // offline mode
        }
    }

    private fun fetchAnimeCharacters(id: Int) {
        ApiClient.apiService.getAnimeCharacters(id).enqueue(object : Callback<AnimeCharacterResponse> {
            override fun onResponse(call: Call<AnimeCharacterResponse>, response: Response<AnimeCharacterResponse>) {
                if (response.isSuccessful) {
                    val characters = response.body()?.data?.take(5)?.map { it.character.name }
                    castText.text = "Main Cast: ${characters?.joinToString(", ")}"
                    response.body()?.let { characterResponse ->
                        val characters = characterResponse.data.take(5).map { it.character.name }
                        castText.text = "Main Cast: ${characters.joinToString(", ")}"
                        saveCharactersToDb(id, characterResponse) // ✅ pass actual object
                    }

                }
            }
            override fun onFailure(call: Call<AnimeCharacterResponse>, t: Throwable) {
                castText.text = "Main Cast: Not available"
            }
        })
    }

    private fun populateUI(data: AnimeDetail) {
        titleText.text = data.title_english ?: data.title
        synopsisText.text = data.synopsis ?: "No synopsis available"
        episodesText.text = "Episodes: ${data.episodes ?: "N/A"}"
        ratingText.text = "Rating: ${data.score ?: "N/A"} (${data.rating ?: "Not Rated"})"

        val genreNames = data.genres.joinToString(", ") { it.name }
        genresText.text = "Genres: $genreNames"

        if (!data.trailer?.embed_url.isNullOrEmpty()) {
            youtubeWebView.settings.javaScriptEnabled = true
            youtubeWebView.loadUrl(data.trailer.embed_url)
            youtubeWebView.visibility = View.VISIBLE
            posterImage.visibility = View.GONE
        } else {
            Glide.with(this)
                .load(data.images.jpg.image_url)
                .into(posterImage)
            posterImage.visibility = View.VISIBLE
            youtubeWebView.visibility = View.GONE
        }

    }

    private fun fetchAnimeDetails(id: Int) {
        ApiClient.apiService.getAnimeDetail(id)
            .enqueue(object : Callback<AnimeDetailResponse> {
                override fun onResponse(
                    call: Call<AnimeDetailResponse>,
                    response: Response<AnimeDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { animeDetail ->
                            populateUI(animeDetail)
                            saveAnimeToDb(animeDetail)
                        }
                    }
                }

                override fun onFailure(call: Call<AnimeDetailResponse>, t: Throwable) {
                    Toast.makeText(this@AnimeDetailActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    private fun saveAnimeToDb(data: AnimeDetail) {

        Thread {
            val animeEntity = AnimeDetailEntity(
                malId = data.mal_id,
                title = data.title,
                titleEnglish = data.title_english,
                titleJapanese = data.title_japanese,
                synopsis = data.synopsis,
                score = data.score,
                episodes = data.episodes,
                rating = data.rating,
                duration = data.duration,
                background = data.background,
                imageUrl = data.images.jpg.image_url,
                trailerUrl = data.trailer?.embed_url,
                genres = data.genres.joinToString(", ") { it.name }
            )
            animeDao.insertAnime(animeEntity)
        }.start()
    }
    private fun saveCharactersToDb(animeId: Int, response: AnimeCharacterResponse) {
        Thread {
            val characterEntities = response.data.map {
                CharacterEntity(animeId = animeId, name = it.character.name)
            }
            animeDao.insertCharacters(characterEntities)
        }.start()
    }
    private fun loadFromDb(id: Int) {
        Thread {
            val anime = animeDao.getAnimeById(id)
            val characters = animeDao.getCharactersForAnime(id)

            runOnUiThread {
                anime?.let { entity ->
                    titleText.text = entity.titleEnglish ?: entity.title
                    synopsisText.text = entity.synopsis ?: "No synopsis available"
                    episodesText.text = "Episodes: ${entity.episodes ?: "N/A"}"
                    ratingText.text = "Rating: ${entity.score ?: "N/A"} (${entity.rating ?: "Not Rated"})"
                    genresText.text = "Genres: ${entity.genres}"

                    if (!entity.trailerUrl.isNullOrEmpty()) {
                        youtubeWebView.settings.javaScriptEnabled = true
                        youtubeWebView.loadUrl(entity.trailerUrl)
                        youtubeWebView.visibility = View.VISIBLE
                        posterImage.visibility = View.GONE
                    } else {
                        Glide.with(this)
                            .load(entity.imageUrl)
                            .into(posterImage)
                        posterImage.visibility = View.VISIBLE
                        youtubeWebView.visibility = View.GONE
                    }
                }
                castText.text = "Main Cast: ${characters.joinToString(", ") { it.name }}"
            }
        }.start()
    }


}
