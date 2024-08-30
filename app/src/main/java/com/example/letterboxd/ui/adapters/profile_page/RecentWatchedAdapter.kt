package com.example.letterboxd.ui.adapters.profile_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.letterboxd.databinding.FakeRecentWatchedBinding
import com.example.letterboxd.domain.model.RecentWatchedFilmItem
import com.squareup.picasso.Picasso
import kotlin.math.min

class RecentWatchedAdapter(
    val removeMovieFunction : (Int, Int) -> Unit,
    val function : (RecentWatchedFilmItem) -> Unit
) : Adapter<RecentWatchedAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : FakeRecentWatchedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            FakeRecentWatchedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private var mainResponse : List<RecentWatchedFilmItem> = emptyList()
    private var longResponse : List<RecentWatchedFilmItem> = emptyList()

    override fun getItemCount(): Int {
        return mainResponse.size
    }

    override fun onBindViewHolder(holder: RecentWatchedAdapter.MyViewHolder, position: Int) {
        val oneReview = mainResponse[position]
        val binding = holder.binding

        Picasso.get().load("https://image.tmdb.org/t/p/w500${oneReview.posterPath}").into(binding.imageView18)
        binding.ratingBar2.rating = oneReview.rating

        binding.root.setOnClickListener {
            function(
                oneReview
            )
        }

        binding.root.setOnLongClickListener {
            removeMovieFunction(oneReview.filmId, position)
            true
        }
    }

    fun updateAdapter(newData : List<RecentWatchedFilmItem>){
        mainResponse = newData.subList(0, min(5, newData.size))
        longResponse = newData
        notifyDataSetChanged()
    }

    fun showAllRecentWatchedMovies(){
        mainResponse = longResponse
        notifyDataSetChanged()
    }

    fun showLessRecentWatchedMovies(){
        mainResponse = longResponse.subList(0, min(5, longResponse.size))
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        val mutableList = longResponse.toMutableList()

        // Remove the item at the given position
        mutableList.removeAt(position)

        // Update longResponse with the new list
        longResponse = mutableList
        mainResponse = longResponse.subList(0, min(5, longResponse.size))
        notifyItemRemoved(position)

        if (longResponse.size > 5) {
            notifyDataSetChanged()
        }
    }
}