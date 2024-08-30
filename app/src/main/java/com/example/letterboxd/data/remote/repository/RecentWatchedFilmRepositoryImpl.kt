package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.local.recent_watched_films.RecentWatchedFilmsDatabase
import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.RecentWatchedFilmItem
import com.example.letterboxd.domain.repository.RecentWatchedFilmRepository
import javax.inject.Inject

class RecentWatchedFilmRepositoryImpl @Inject constructor(
    val api : MyApi,
    val database : RecentWatchedFilmsDatabase,
) : RecentWatchedFilmRepository {
    override suspend fun getRecentWatchedFilms(): List<RecentWatchedFilmItem> {
        val recentWatchedFilmsDao = database.recentWatchedFilmsDao
        val result = recentWatchedFilmsDao.getAllRecentWatchedFilms()
        val newList = mutableListOf<RecentWatchedFilmItem>()
        result.forEach {
            val resultFromApi = api.getMovieDetails(it.idFilm)
            newList.add(
                RecentWatchedFilmItem(
                    posterPath = resultFromApi.posterPath,
                    rating = it.rating,
                    filmId = resultFromApi.id,
                    watchedDate = it.dateWatched
                )
            )
        }
        return newList
    }
}