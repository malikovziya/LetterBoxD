package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.FilmCreditsItem

interface FilmCrewRepository {
    suspend fun getFilmCrew(id : Int) : List<FilmCreditsItem>
}