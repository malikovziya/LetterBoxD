package com.example.letterboxd.ui.adapters.left_navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.R
import com.example.letterboxd.common.utils.roundFloats
import com.example.letterboxd.data.local.watchlist.WatchlistEntity
import com.example.letterboxd.databinding.WatchlistItemBinding
import com.squareup.picasso.Picasso

class WatchlistAdapter(
    val goToMovie : (Int) -> Unit,
    val deleteMovieFromWatchlist : (Int) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: WatchlistItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            WatchlistItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private var watchlistList : List<WatchlistEntity> = emptyList()

    override fun getItemCount(): Int {
        return watchlistList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneFilm = watchlistList[position]
        val binding = holder.binding

        binding.textMovieName.text = oneFilm.movieName
        binding.textRating.text = roundFloats(oneFilm.movieRating.div(2), 1).toString()
        binding.textDuration.text = oneFilm.movieDuration
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneFilm.moviePath)
            .into(binding.imageMovie)

        binding.root.setOnClickListener {
            goToMovie(oneFilm.movieId)
        }

        binding.root.setOnLongClickListener {
            showPopupMenu(view = it, filmId = oneFilm.movieId)
            true
        }
    }

    fun updateAdapter(newData : List<WatchlistEntity>){
        watchlistList = newData
        notifyDataSetChanged()
    }

    private fun showPopupMenu(view: View, filmId : Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.right_click_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.remove -> {
                    deleteMovieFromWatchlist(filmId)
                    true
                }
                else -> {
                    false
                }
            }
        }

        popupMenu.show()
    }
}


