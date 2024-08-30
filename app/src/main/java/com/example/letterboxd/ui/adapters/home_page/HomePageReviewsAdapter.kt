package com.example.letterboxd.ui.adapters.home_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.R
import com.example.letterboxd.databinding.HomepageReviewLayoutBinding
import com.example.letterboxd.domain.model.FriendsReviewItem
import com.example.letterboxd.domain.model.ReviewDetails
import com.squareup.picasso.Picasso

class HomePageReviewsAdapter(
    val goToReview : (ReviewDetails) -> Unit,
    val goToMovieById : (Int) -> Unit
) : RecyclerView.Adapter<HomePageReviewsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding : HomepageReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            HomepageReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    private var response : List<FriendsReviewItem> = emptyList()

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val oneComment = response[position]
        val binding = holder.binding

        with(binding){
            textName.text = oneComment.filmName
            textCommentor.text = oneComment.reviewMaker
            textYear.text = oneComment.year.split("-").first()
            createdTime.text = oneComment.reviewDate.split("T").first()
            textComment.text = oneComment.comment
            if (oneComment.rating != null) ratingBar.rating = oneComment.rating.div(2)
            else ratingBar.visibility = View.GONE
            if (oneComment.profilePath.isNullOrEmpty()) imageProfile.setImageResource(R.drawable.anonymous_user)
            else {
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneComment.profilePath)
                    .into(imageProfile)
            }
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + oneComment.moviePicturePath)
                .into(imageFilm)
        }

        binding.imageFilm.setOnClickListener {
            goToMovieById(oneComment.idMovie)
        }

        binding.root.setOnClickListener {
            goToReview(
                ReviewDetails(
                    movieImage = oneComment.movieBackgroundPath,
                    profileImage = oneComment.profilePath,
                    reviewId = oneComment.reviewId
                )
            )
        }

        binding.textReadMore.setOnClickListener {
            if (binding.textReadMore.text == "READ MORE >>") {
                    binding.textComment.maxLines = Int.MAX_VALUE
                    binding.textReadMore.text = "READ LESS <<"
                } else {
                    binding.textComment.maxLines = 7
                    binding.textReadMore.text = "READ MORE >>"
                }
        }

        if (oneComment.comment.length <= 282){
            binding.textReadMore.visibility = View.GONE
        } else binding.textReadMore.visibility = View.VISIBLE
    }

    fun updateAdapter(newData : List<FriendsReviewItem>){
        response = newData
        notifyDataSetChanged()
    }
}
