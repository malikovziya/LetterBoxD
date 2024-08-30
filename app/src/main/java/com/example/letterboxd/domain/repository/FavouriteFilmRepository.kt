package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.FavouriteFilmItem

interface FavouriteFilmRepository {
    suspend fun getFavouriteFilms(): List<FavouriteFilmItem>
}