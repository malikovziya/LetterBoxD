package com.example.letterboxd.ui.view_models.bottom_sheets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.data.local.watchlist.WatchlistDao
import com.example.letterboxd.data.local.watchlist.WatchlistEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    val watchlistDao : WatchlistDao
) : ViewModel() {
    val watchlistMoviesToDisplay = MutableStateFlow<List<WatchlistEntity>>(emptyList())

    fun getWatchlistMovies(){
        viewModelScope.launch {
            val watchlistMovies = watchlistDao.getWatchlist()
            watchlistMoviesToDisplay.emit(watchlistMovies)
        }
    }
}