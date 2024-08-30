package com.example.letterboxd.ui.view_models.bottom_sheets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.domain.model.LikedMovieItem
import com.example.letterboxd.domain.repository.LikedMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedMoviesViewModel @Inject constructor(
    val repository : LikedMoviesRepository,
    val dao : FavouriteFilmsDao
) : ViewModel() {
    val filmsToDisplay = MutableStateFlow<List<LikedMovieItem>>(emptyList())

    fun getLikedMovies() {
        viewModelScope.launch {
            val result = repository.getLikedMovies()
            filmsToDisplay.emit(result)
        }
    }

    fun removeLikedMovie(id : Int){
        viewModelScope.launch {
            val film = dao.determineIfFilmAlreadyInFavourites(id)
            if (film != null){
                dao.deleteFavouriteFilm(film)
            }

            delay(1000)
            getLikedMovies()
        }
    }
}