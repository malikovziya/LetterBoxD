package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.FilmReviewItem
import com.example.letterboxd.domain.repository.FilmReviewsRepository
import javax.inject.Inject

class FilmReviewsRepositoryImpl @Inject constructor(val api : MyApi): FilmReviewsRepository {
    override suspend fun getFilmReviews(filmId: Int): List<FilmReviewItem> {
        val response = api.getFilmReviews(filmId)
        return response.results.map {
            FilmReviewItem(
                imagePath = it.authorDetails.avatarPath,
                reviewMakerName = it.author,
                givenRating = it.authorDetails.rating,
                creationTime = it.createdAt,
                comment = it.content,
                reviewId = it.id
            )
        }
    }
}