package com.example.letterboxd.ui.view_models.bottom_sheets

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.letterboxd.R
import com.example.letterboxd.common.utils.getRotationDegrees
import com.example.letterboxd.common.utils.rotateBitmap
import com.example.letterboxd.domain.model.ReviewItem
import com.example.letterboxd.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val reviewRepository : ReviewRepository,
    @Named("username") val sharedPrefUsername : SharedPreferences,
    @Named("profile_picture") val sharedPreferences: SharedPreferences
): ViewModel() {
    val reviewsToDisplay = MutableStateFlow<List<ReviewItem>>(emptyList())
    val profilePhoto = MutableStateFlow<Bitmap?>(null)
    val usernameToDisplay = MutableStateFlow<String?>(null)

    fun getReviews(){
        viewModelScope.launch {
            val reviews = reviewRepository.getReviews()
            reviewsToDisplay.emit(reviews)
        }
    }

    fun getProfilePhoto(context: Context){
        viewModelScope.launch {
            val profileImage = sharedPreferences.getString("profile_picture_path", null)

            val imageFile = File(context.filesDir, profileImage)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            val rotationDegrees = getRotationDegrees(imageFile.absolutePath)
            val rotatedBitmap = rotateBitmap(bitmap, rotationDegrees)

            profilePhoto.emit(rotatedBitmap)
        }
    }

    fun getUser(){
        val user = sharedPrefUsername.getString("username", null)
        viewModelScope.launch {
            usernameToDisplay.emit(user)
        }
    }
}