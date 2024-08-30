package com.example.letterboxd.ui.adapters.home_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.R
import com.example.letterboxd.databinding.CategoryFilmLayoutBinding
import com.example.letterboxd.domain.model.CategoryItem
import com.squareup.picasso.Picasso

class HomePageCategoriesAdapter(
    val onClick : (CategoryItem) -> Unit
) : RecyclerView.Adapter<HomePageCategoriesAdapter.MyCategoryViewHolder>() {
    class MyCategoryViewHolder(val binding : CategoryFilmLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
        return MyCategoryViewHolder(
            CategoryFilmLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var response : List<CategoryItem> = emptyList()

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        val oneItem = response[position]
        val binding = holder.binding

        with(binding){
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneItem.image1)
                .placeholder(R.drawable.film_placeholder)
                .into(imageView14)
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneItem.image2)
                .into(imageView13)
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneItem.image3)
                .into(imageView12)
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneItem.image4)
                .into(imageView11)
        }

        binding.root.setOnClickListener {
            onClick(oneItem)
        }
    }

    fun updateAdapter(newData : List<CategoryItem>){
        response = newData
        notifyDataSetChanged()
    }
}