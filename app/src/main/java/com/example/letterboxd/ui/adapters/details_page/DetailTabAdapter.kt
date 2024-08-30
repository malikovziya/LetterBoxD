package com.example.letterboxd.ui.adapters.details_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.databinding.StringRvItemBinding

class DetailTabAdapter : RecyclerView.Adapter<DetailTabAdapter.MyViewHolder>() {
    class MyViewHolder(val binding : StringRvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            StringRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    var response : List<String> = emptyList()

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneString = response[position]
        val binding = holder.binding

        binding.textView8.text = oneString
    }

    fun updateAdapter(newData : List<String>){
        response = newData
        notifyDataSetChanged()
    }
}