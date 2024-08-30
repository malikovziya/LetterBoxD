package com.example.letterboxd.ui.adapters.left_navigation

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.letterboxd.R
import com.example.letterboxd.databinding.ProfileRecentReviewLayoutBinding
import com.example.letterboxd.domain.model.ReviewItem
import com.squareup.picasso.Picasso

class ReviewsAdapter(
    private var profileImage : Bitmap?,
    private var reviewOwner : String?,
    val goToMovie : (Int) -> Unit
) : Adapter<ReviewsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : ProfileRecentReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ProfileRecentReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return response.size
    }

    private var response : List<ReviewItem> = emptyList()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneReview = response[position]
        val binding = holder.binding

        binding.textName.text = oneReview.filmName
        binding.textYear.text = oneReview.filmReleaseDate.split("-").first()
        if (reviewOwner == null){
            binding.textCommentor.text = "Anonymous"
        } else {
            binding.textCommentor.text = reviewOwner
        }
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

    fun updateAdapter(newData : List<ReviewItem>) {
        response = newData
        notifyDataSetChanged()
    }

    fun updatePhotos(newData : Bitmap?){
        if (newData == null) return
        profileImage = newData

        notifyDataSetChanged()
    }

    fun updateReviewMaker(newOwner : String?){
        if (newOwner == null) return
        reviewOwner = newOwner

        notifyDataSetChanged()
    }
}