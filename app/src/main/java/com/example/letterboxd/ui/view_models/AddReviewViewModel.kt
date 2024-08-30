package com.example.letterboxd.ui.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.data.local.reviews.ReviewDao
import com.example.letterboxd.data.local.reviews.ReviewEntity
import com.example.letterboxd.databinding.FragmentAddReviewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(
    val favFilmsDao : FavouriteFilmsDao,
    private val reviewDao : ReviewDao
) : ViewModel() {
    val loading = MutableStateFlow(false)

    fun publishReview(context : Context, binding : FragmentAddReviewBinding, reviewText : String, rating : Float, reviewDate : String, filmId : Int){
        viewModelScope.launch {
            loading.value = true
            delay(500)
            reviewDao.insertReview(
                ReviewEntity(
                    filmId = filmId,
                    comment = reviewText,
                    rating = rating,
                    reviewDate = reviewDate,
                )
            )
            FancyToast.makeText(context, "Your review has been published", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
            binding.editText.text = null
            binding.scaleRatingBar.rating = 0f
            loading.value = false
        }
    }
}