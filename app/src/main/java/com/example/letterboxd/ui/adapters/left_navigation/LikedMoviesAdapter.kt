package com.example.letterboxd.ui.adapters.left_navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.R
import com.example.letterboxd.databinding.LikedMovieItemBinding
import com.example.letterboxd.domain.model.LikedMovieItem
import com.squareup.picasso.Picasso

class LikedMoviesAdapter(
    val function : (LikedMovieItem) -> Unit,
    val removeLike : (Int) -> Unit
) : RecyclerView.Adapter<LikedMoviesAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : LikedMovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LikedMovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private var response : List<LikedMovieItem> = emptyList()

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneItem = response[position]

        val binding = holder.binding

        Picasso.get().load("https://image.tmdb.org/t/p/w500${oneItem.movieImagePath}")
            .into(binding.imageFilmPosterPath)

        binding.imageButtonLike.setImageResource(R.drawable.heart_red)
        binding.textFilmTitle.text = oneItem.movieName

        binding.root.setOnClickListener {
            function(
                LikedMovieItem(
                    movieImagePath = oneItem.movieImagePath,
                    movieId = oneItem.movieId,
                    movieName = oneItem.movieName
                )
            )
        }

        binding.imageButtonLike.setOnClickListener {
            binding.imageButtonLike.setImageResource(R.drawable.heart_empty)
            binding.imageButtonLike.postDelayed({
                removeLike(oneItem.movieId)
            }, 700)
        }
    }

    fun updateAdapter(data : List<LikedMovieItem>){
        response = data
        notifyDataSetChanged()
    }
}
