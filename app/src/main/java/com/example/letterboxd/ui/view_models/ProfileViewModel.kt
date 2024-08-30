package com.example.letterboxd.ui.view_models

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.data.local.recent_watched_films.RecentWatchedFilmsDao
import com.example.letterboxd.data.local.reviews.ReviewDao
import com.example.letterboxd.domain.model.FavouriteFilmItem
import com.example.letterboxd.domain.model.RecentWatchedFilmItem
import com.example.letterboxd.domain.model.ReviewItem
import com.example.letterboxd.domain.repository.FavouriteFilmRepository
import com.example.letterboxd.domain.repository.RecentWatchedFilmRepository
import com.example.letterboxd.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.Year
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository1 : FavouriteFilmRepository,
    private val repository2 : ReviewRepository,
    private val repository3 : RecentWatchedFilmRepository,
    @Named("profile_picture") val sharedPreferencesPicture: SharedPreferences,
    @Named("profile_background") val sharedPreferencesBackground : SharedPreferences,
    @Named("name") val sharedPreferencesName: SharedPreferences,
    private val daoFavFilms : FavouriteFilmsDao,
    private val daoWatchedFilms : RecentWatchedFilmsDao,
    val daoReviews : ReviewDao
) : ViewModel() {

    val favouriteFilmsToDisplay = MutableStateFlow<List<FavouriteFilmItem>>(emptyList())
    val reviewsToDisplay = MutableStateFlow<List<ReviewItem>>(emptyList())
    val recentWatchedFilmsToDisplay = MutableStateFlow<List<RecentWatchedFilmItem>>(emptyList())

    var yearlyFilmList = MutableStateFlow<List<RecentWatchedFilmItem>>(emptyList())
    val profilePictureBitmap = MutableStateFlow<Bitmap?>(null)
    val backgroundImageBitmap = MutableStateFlow<Bitmap?>(null)
    val name = MutableStateFlow<String?>(null)

    fun getAllFavouriteFilms(){
        viewModelScope.launch {
            val result = repository1.getFavouriteFilms()
            favouriteFilmsToDisplay.emit(result)
        }
    }

    fun getAllReviews(){
        viewModelScope.launch {
            val result = repository2.getReviews()
            reviewsToDisplay.emit(result)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllRecentWatchedFilms(){
        viewModelScope.launch {
            val result = repository3.getRecentWatchedFilms()
            recentWatchedFilmsToDisplay.emit(result)

            val listThisYear = result.filter {
                it.watchedDate.split(", ")[1].toInt() == Year.now().value
            }

            yearlyFilmList.emit(listThisYear)
        }
    }

    fun removeLikedMovie(id : Int){
        viewModelScope.launch {
            val film = daoFavFilms.determineIfFilmAlreadyInFavourites(id)
            if (film != null){
                daoFavFilms.deleteFavouriteFilm(film)
            }

            delay(500)
            getAllFavouriteFilms()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeWatchedMovie(id : Int){
        viewModelScope.launch {
            val film = daoWatchedFilms.returnRecentFilmById(id)
            if (film != null){
                daoWatchedFilms.deleteWatchedFilm(film)
            }

            delay(500)
            getAllRecentWatchedFilms()
        }
    }

    fun removeReview(id : Int){
        viewModelScope.launch {
            val review = daoReviews.getReviewById(id)
            if (review != null) {
                daoReviews.deleteReview(review)
            }
        }
    }

    fun getProfilePictureBitmap(context: Context, uri: Uri) {
        viewModelScope.launch {
            val uniqueImageName = "profile_picture_${System.currentTimeMillis()}.jpg"
            val imageFile = File(context.filesDir, uniqueImageName)

            try {
                val oldImagePath = sharedPreferencesPicture.getString("profile_picture_path", null)
                if (oldImagePath != null) {
                    val oldImageFile = File(context.filesDir, oldImagePath)
                    if (oldImageFile.exists()) {
                        oldImageFile.delete()
                    }
                }

                // Save the new image
                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(imageFile)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()

                sharedPreferencesPicture.edit().putString("profile_picture_path", uniqueImageName).apply()

                val bitmapImage = withContext(Dispatchers.IO) {
                    Glide.with(context).asBitmap().load(imageFile).submit().get()
                }

                profilePictureBitmap.emit(bitmapImage)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving profile picture: ${e.message}")
            }
        }
    }

    fun getBackgroundImageBitmap(context: Context, uri: Uri) {
        viewModelScope.launch {
            val uniqueImageName = "background_picture_${System.currentTimeMillis()}.jpg"
            val imageFile = File(context.filesDir, uniqueImageName)

            try {
                val oldImagePath = sharedPreferencesBackground.getString("profileBackground", null)
                if (oldImagePath != null) {
                    val oldImageFile = File(context.filesDir, oldImagePath)
                    if (oldImageFile.exists()) {
                        oldImageFile.delete()
                    }
                }

                // Save the new image
                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(imageFile)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()

                sharedPreferencesBackground.edit().putString("profileBackground", uniqueImageName).apply()

                val bitmapImage = withContext(Dispatchers.IO) {
                    Glide.with(context).asBitmap().load(imageFile).submit().get()
                }

                backgroundImageBitmap.emit(bitmapImage)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving background picture: ${e.message}")
            }
        }
    }
    fun getName(){
        viewModelScope.launch {
            val result = sharedPreferencesName.getString("name", "you")
            name.emit(result)
        }
    }
}