package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.PopularFilmItem

interface PopularFilmRepository {
    suspend fun getPopularFilms() : List<PopularFilmItem>
}