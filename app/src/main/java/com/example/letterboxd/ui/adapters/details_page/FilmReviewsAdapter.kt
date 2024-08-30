package com.example.letterboxd.ui.adapters.details_page

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.R
import com.example.letterboxd.databinding.FakeFilmReviewBinding
import com.example.letterboxd.databinding.FragmentFilmDetailsBinding
import com.example.letterboxd.domain.model.FilmReviewItem
import com.example.letterboxd.domain.model.ReviewDetails
import com.squareup.picasso.Picasso
import kotlin.math.min


class FilmReviewsAdapter (
    val goToReview : (ReviewDetails) -> Unit
) : RecyclerView.Adapter<FilmReviewsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : FakeFilmReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FakeFilmReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var mainResult : List<FilmReviewItem> = emptyList()
    private var longResult : List<FilmReviewItem> = emptyList()

    override fun getItemCount(): Int {
        return mainResult.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneReview = mainResult[position]
        val binding = holder.binding

        with(binding){
            textCommentor.text = oneReview.reviewMakerName

            if (oneReview.givenRating == null){
                ratingBar.visibility = View.GONE
            } else{
                ratingBar.rating = oneReview.givenRating.div(2).toFloat()
            }

            createdTime.text = oneReview.creationTime.split("T").first()
            textComment.text = oneReview.comment
            if (oneReview.imagePath == null) {
                imageProfile.setImageResource(R.drawable.anonymous_user)
            } else {
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneReview.imagePath)
                    .into(imageProfile)
            }

            if (oneReview.comment.length <= 290){
                textReadMore.visibility = View.GONE
            } else textReadMore.visibility = View.VISIBLE
        }

        binding.root.setOnClickListener {
            goToReview(
                ReviewDetails(
                    movieImage = "",
                    profileImage = oneReview.imagePath,
                    reviewId = oneReview.reviewId
                )
            )
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
    }

    fun updateAdapter(newData : List<FilmReviewItem>){
        mainResult = newData.subList(0, min(newData.size, 3))
        longResult = newData
        notifyDataSetChanged()
    }

    fun showAllReviews(){
        mainResult = longResult
        notifyDataSetChanged()
    }

    fun seeLessReviews(){
        mainResult = longResult.subList(0, min(longResult.size, 3))
        notifyDataSetChanged()
    }
}