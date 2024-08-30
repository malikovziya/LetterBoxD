package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.ExploreFilmItem

interface ExplorePageRepository {
    suspend fun getExplorePageFilms(): List<ExploreFilmItem>
}