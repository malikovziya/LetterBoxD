package com.example.letterboxd.ui.adapters.details_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.databinding.CastOfFilmBinding
import com.example.letterboxd.domain.model.FilmCreditsItem
import com.squareup.picasso.Picasso

class FilmCrewsAdapter(
    val goToCreditDetails : (FilmCreditsItem) -> Unit
) : RecyclerView.Adapter<FilmCrewsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : CastOfFilmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CastOfFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var result : List<FilmCreditsItem> = emptyList()

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneItem = result[position]
        val binding = holder.binding

        Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneItem.imagePath).into(binding.imageView7)

        binding.root.setOnClickListener {
            goToCreditDetails(
                FilmCreditsItem(
                    creditId = oneItem.creditId,
                    imagePath = oneItem.imagePath,
                    character = oneItem.character,
                    department = oneItem.department,
                    popularity = oneItem.popularity,
                    name = oneItem.name,
                    gender = oneItem.gender
                )
            )
        }
    }

    fun updateAdapter(newData : List<FilmCreditsItem>){
        result = newData
        notifyDataSetChanged()
    }
}