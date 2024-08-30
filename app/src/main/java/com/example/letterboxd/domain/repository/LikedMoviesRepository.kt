package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.LikedMovieItem

interface LikedMoviesRepository {
    suspend fun getLikedMovies() : List<LikedMovieItem>
}