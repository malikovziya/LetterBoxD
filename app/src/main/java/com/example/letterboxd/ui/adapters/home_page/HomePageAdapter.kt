package com.example.letterboxd.ui.adapters.home_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.R
import com.example.letterboxd.databinding.PopularFilmLayoutBinding
import com.example.letterboxd.domain.model.PopularFilmItem
import com.squareup.picasso.Picasso

class HomePageAdapter(val function : (PopularFilmItem) -> Unit) : RecyclerView.Adapter<HomePageAdapter.MyViewHolder>() {
    class MyViewHolder(val binding : PopularFilmLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            PopularFilmLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var response : List<PopularFilmItem> = emptyList()

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val picture = response[position]
        val binding = holder.binding

        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + picture.urlPath)
            .placeholder(R.drawable.img_1)
            .into(binding.imageViewPopularFilm)


        binding.root.setOnClickListener {
            function(
                PopularFilmItem(
                    id = picture.id,
                    urlPath = picture.urlPath
                )
            )
        }
    }

    fun updateAdapter(newData : List<PopularFilmItem>){
        response = newData
        notifyDataSetChanged()
    }
}
