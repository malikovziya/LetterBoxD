package com.example.letterboxd.ui.view_models.bottom_sheets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.domain.model.RecentWatchedFilmItem
import com.example.letterboxd.domain.repository.RecentWatchedFilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchedMoviesViewModel @Inject constructor(
    private val repository : RecentWatchedFilmRepository,
): ViewModel() {
    val watchedMoviesToDisplay = MutableStateFlow<List<RecentWatchedFilmItem>>(emptyList())

    fun getWatchedMovies() {
        viewModelScope.launch {
            val watchedFilms = repository.getRecentWatchedFilms()
            watchedMoviesToDisplay.emit(watchedFilms)
        }
    }
}