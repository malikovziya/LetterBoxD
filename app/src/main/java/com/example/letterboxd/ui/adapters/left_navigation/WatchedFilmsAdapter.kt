package com.example.letterboxd.ui.adapters.left_navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.databinding.WatchedMovieItemBinding
import com.example.letterboxd.domain.model.RecentWatchedFilmItem
import com.squareup.picasso.Picasso

class WatchedFilmsAdapter(
    val function : (RecentWatchedFilmItem) -> Unit
) : RecyclerView.Adapter<WatchedFilmsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : WatchedMovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            WatchedMovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private var response : List<RecentWatchedFilmItem> = emptyList()

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneReview = response[position]
        val binding = holder.binding

        Picasso.get().load("https://image.tmdb.org/t/p/w500${oneReview.posterPath}").into(binding.imageViewFilm)
        binding.scaleRatingBarFilm.rating = oneReview.rating

        binding.root.setOnClickListener {
            function(oneReview)
        }
    }

    fun updateAdapter(newData : List<RecentWatchedFilmItem>){
        response = newData
        notifyDataSetChanged()
    }
}