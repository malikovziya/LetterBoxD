package com.example.letterboxd.ui.view_models

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.data.local.recent_watched_films.RecentWatchedFilmsDao
import com.example.letterboxd.data.local.watchlist.WatchlistDao
import com.example.letterboxd.data.remote.model.Person
import com.example.letterboxd.domain.model.DetailsTabItem
import com.example.letterboxd.domain.model.FilmCreditsItem
import com.example.letterboxd.domain.model.FilmReviewItem
import com.example.letterboxd.domain.repository.DetailsTabRepository
import com.example.letterboxd.domain.repository.FilmCastsRepository
import com.example.letterboxd.domain.repository.FilmCrewRepository
import com.example.letterboxd.domain.repository.FilmDetailsRepository
import com.example.letterboxd.domain.repository.FilmReviewsRepository
import com.example.letterboxd.domain.repository.PersonDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    val repository: FilmDetailsRepository,
    val repository2: FilmCastsRepository,
    val repository3: FilmCrewRepository,
    val repository4: FilmReviewsRepository,
    val repository5 : DetailsTabRepository,
    val repository6 : PersonDetailsRepository,
    val likedMoviesDao : FavouriteFilmsDao,
    val recentWatchedFilmsDao : RecentWatchedFilmsDao,
    val watchlistDao: WatchlistDao,
    @Named("review_details")
    val sharedPref : SharedPreferences
): ViewModel() {
    val filmCasts = MutableStateFlow<List<FilmCreditsItem>>(emptyList())
    val filmCrews = MutableStateFlow<List<FilmCreditsItem>>(emptyList())
    val filmReviews = MutableStateFlow<List<FilmReviewItem>>(emptyList())
    val detailsTabData = MutableStateFlow<DetailsTabItem?>(null)

    fun getFilmCasts(filmId : Int){
        viewModelScope.launch {
            val result = repository2.getFilmCasts(filmId)
            filmCasts.emit(result)
        }
    }

    fun getFilmCrew(filmId : Int) {
        viewModelScope.launch {
            val result = repository3.getFilmCrew(filmId)
            filmCrews.emit(result)
        }
    }

    fun getFilmReviews(filmId : Int) {
        viewModelScope.launch {
            val result = repository4.getFilmReviews(filmId)
            filmReviews.emit(result)
        }
    }

    fun getDetailsTabData(filmId : Int){
        viewModelScope.launch {
            val result = repository5.getDetailsTabData(filmId)
            detailsTabData.emit(result)
        }
    }
}