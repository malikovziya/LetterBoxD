package com.example.letterboxd.ui.adapters.profile_page

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.R
import com.example.letterboxd.data.local.reviews.ReviewEntity
import com.example.letterboxd.databinding.FragmentProfileBinding
import com.example.letterboxd.databinding.ProfileRecentReviewLayoutBinding
import com.example.letterboxd.domain.model.ReviewItem
import com.squareup.picasso.Picasso
import kotlin.math.min

class RecentReviewsAdapter(
    private var profileImage : Bitmap?,
    var name : String,
    val goToMovie : (Int) -> Unit
) : RecyclerView.Adapter<RecentReviewsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : ProfileRecentReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ProfileRecentReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mainResponse.size
    }

    var mainResponse : List<ReviewItem> = emptyList()
    var longResponse : List<ReviewItem> = emptyList()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneReview = mainResponse[position]
        val binding = holder.binding

        binding.textName.text = oneReview.filmName
        binding.textYear.text = oneReview.filmReleaseDate.split("-").first()
        binding.textCommentor.text = name
        binding.ratingBar.rating = oneReview.rating
        binding.createdTime.text = oneReview.reviewDate
        binding.textComment.text = oneReview.reviewContent
        Picasso.get().load("https://image.tmdb.org/t/p/w500${oneReview.filmImage}").into(binding.imageFilm)
        if (profileImage == null) {
            binding.imageProfile.setImageResource(R.drawable.anonymous_user)
        } else {
            binding.imageProfile.setImageBitmap(profileImage)
        }

        binding.imageFilm.setOnClickListener {
            goToMovie(oneReview.filmId)
        }

        binding.textReadMore.setOnClickListener {
            if (binding.textReadMore.text == "READ MORE >>") {
                binding.textComment.maxLines = Int.MAX_VALUE
                binding.textReadMore.text = "READ LESS <<"
            } else {
                binding.textComment.maxLines = 5
                binding.textReadMore.text = "READ MORE >>"
            }
        }

        if (oneReview.reviewContent.length <= 290){
            binding.textReadMore.visibility = View.GONE
        } else binding.textReadMore.visibility = View.VISIBLE
    }

    fun updatePhotos(newData : Bitmap?){
        if (newData == null) return
        profileImage = newData

        notifyDataSetChanged()
    }

    fun updateName(name : String){
        this.name = name
        notifyDataSetChanged()
    }

    fun updateAdapter(newData : List<ReviewItem>) {
        mainResponse = newData.subList(0, min(3, newData.size))
        longResponse = newData
        notifyDataSetChanged()
    }

    fun showAllReviews() {
        mainResponse = longResponse
        notifyDataSetChanged()
    }

    fun showLessReviews(){
        mainResponse = longResponse.subList(0, min(3, longResponse.size))
        notifyDataSetChanged()
    }

    fun handleSwipe(position: Int) {
        val mutableList = longResponse.toMutableList()

        // Remove the item at the given position
        mutableList.removeAt(position)

        // Update longResponse with the new list
        longResponse = mutableList
        mainResponse = longResponse.subList(0, min(3, longResponse.size))
        notifyItemRemoved(position)

        if (longResponse.size > 3) {
            notifyDataSetChanged()
        }
    }

    fun handleUndo(position : Int, removedMovie : ReviewItem){
        val mutableList = longResponse.toMutableList()

        mutableList.add(position, removedMovie)
        longResponse = mutableList
        mainResponse = longResponse.subList(0, min(3, longResponse.size))

        notifyItemInserted(position)

        if (longResponse.size > 3) {
            notifyDataSetChanged()
        }

    }
}

