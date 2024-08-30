package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.RecentWatchedFilmItem

interface RecentWatchedFilmRepository {
    suspend fun getRecentWatchedFilms(): List<RecentWatchedFilmItem>
}