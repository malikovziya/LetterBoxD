package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.DetailScreen

interface FilmDetailsRepository {
    suspend fun getFilmDetails(id: Int): DetailScreen
}