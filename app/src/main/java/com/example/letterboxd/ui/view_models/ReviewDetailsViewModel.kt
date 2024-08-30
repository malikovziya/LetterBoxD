package com.example.letterboxd.ui.view_models

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.domain.model.ReviewDetails
import com.example.letterboxd.domain.model.ReviewDetailsItem
import com.example.letterboxd.domain.repository.ReviewDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ReviewDetailsViewModel @Inject constructor(
    private val repo : ReviewDetailsRepository,
    @Named("review_details")
    val sharedPrefs : SharedPreferences
): ViewModel() {
    var reviewDetails = MutableStateFlow<ReviewDetailsItem?>(null)

    fun getReviewDetails(id : String){
        viewModelScope.launch {
            val result = repo.getReviewDetails(id)
            reviewDetails.emit(result)
        }
    }
}