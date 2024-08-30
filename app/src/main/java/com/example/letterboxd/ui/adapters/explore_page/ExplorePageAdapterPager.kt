package com.example.letterboxd.ui.adapters.explore_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.data.remote.model.Result
import com.example.letterboxd.databinding.ExploreFilmLayoutBinding
import com.squareup.picasso.Picasso

class ExplorePageAdapterPager(val function: (Result) -> Unit) : PagingDataAdapter<Result, ExplorePageAdapterPager.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ExploreFilmLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ExploreFilmLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    companion object {
        private val DIFF_CALLBACK = object : ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding

        if (item != null) {
            binding.filmTitle.text = item.title
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + item.posterPath).into(binding.filmImage)

        }

        binding.root.setOnClickListener {
            if (item != null) {
                function(item)
            }
        }
    }
}
