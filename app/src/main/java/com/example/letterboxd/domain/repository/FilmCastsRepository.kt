package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.FilmCreditsItem

interface FilmCastsRepository {
    suspend fun getFilmCasts(id : Int) : List<FilmCreditsItem>
}