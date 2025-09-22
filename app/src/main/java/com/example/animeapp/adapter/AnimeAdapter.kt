package com.example.animeapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animeapp.R
import com.example.animeapp.model.Anime
import com.example.animeapp.screen.AnimeDetailActivity

class AnimeAdapter(private val animeList: List<Anime>) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anime, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.title.text = anime.title
        holder.episodes.text = "Episodes: ${anime.episodes ?: "N/A"}"
        holder.rating.text = "Rating: ${anime.score ?: "N/A"}"
        Glide.with(holder.itemView.context)
            .load(anime.images.jpg.image_url)
            .into(holder.posterImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AnimeDetailActivity::class.java)
            intent.putExtra("anime_id", anime.mal_id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = animeList.size

    inner class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val episodes: TextView = itemView.findViewById(R.id.episodes)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val posterImage: ImageView = itemView.findViewById(R.id.posterImage)
    }
}
