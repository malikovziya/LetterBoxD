package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.FilmReviewItem

interface FilmReviewsRepository {
    suspend fun getFilmReviews(filmId: Int): List<FilmReviewItem>
}