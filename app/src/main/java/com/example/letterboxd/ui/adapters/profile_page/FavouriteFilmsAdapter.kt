package com.example.letterboxd.ui.adapters.profile_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.letterboxd.databinding.FakeFavouriteFilmBinding
import com.example.letterboxd.domain.model.FavouriteFilmItem
import com.squareup.picasso.Picasso

class FavouriteFilmsAdapter(
    val removeLikeFunction : (Int, Int) -> Unit,
    val function : (FavouriteFilmItem) -> Unit
) : Adapter<FavouriteFilmsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : FakeFavouriteFilmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FakeFavouriteFilmBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private var response : List<FavouriteFilmItem> = emptyList()

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneItem = response[position]

        val binding = holder.binding

        Picasso.get().load("https://image.tmdb.org/t/p/w500${oneItem.filmImage}")
            .into(binding.imageView17)

        binding.root.setOnClickListener {
            function(
                FavouriteFilmItem(
                    filmImage = oneItem.filmImage,
                    id = oneItem.id,
                )
            )
        }

        binding.root.setOnLongClickListener {
            removeLikeFunction(oneItem.id, position)
            true
        }
    }

    fun updateAdapter(data : List<FavouriteFilmItem>){
        response = data
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        val mutableList = response.toMutableList()
        mutableList.removeAt(position)
        response = mutableList
        notifyItemRemoved(position)
    }
}
