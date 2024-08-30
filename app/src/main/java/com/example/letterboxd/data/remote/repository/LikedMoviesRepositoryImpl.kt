package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.domain.model.LikedMovieItem
import com.example.letterboxd.domain.repository.LikedMoviesRepository
import javax.inject.Inject

class LikedMoviesRepositoryImpl @Inject constructor(
    private val dao : FavouriteFilmsDao
) : LikedMoviesRepository {
    override suspend fun getLikedMovies(): List<LikedMovieItem> {
        val response = dao.getAllFavouriteFilms()

        val newList = mutableListOf<LikedMovieItem>()

        response.forEach {
            newList.add(
                LikedMovieItem(
                    movieId = it.id,
                    movieImagePath = it.imagePathMovie,
                    movieName = it.nameMovie
                )
            )
        }
        return newList
    }
}