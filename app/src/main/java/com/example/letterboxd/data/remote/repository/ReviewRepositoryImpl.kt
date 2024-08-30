package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.local.reviews.ReviewDatabase
import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.ReviewItem
import com.example.letterboxd.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    val api : MyApi,
    private val database : ReviewDatabase
) : ReviewRepository {
    override suspend fun getReviews(): List<ReviewItem> {
        val reviewDao = database.reviewDao

        val result = reviewDao.getAllReviews()
        val newList = mutableListOf<ReviewItem>()
        result.forEach{
            val filmDetails = api.getMovieDetails(it.filmId)
            newList.add(
                ReviewItem(
                    filmImage = filmDetails.posterPath,
                    reviewContent = it.comment,
                    reviewDate = it.reviewDate,
                    rating = it.rating,
                    filmName = filmDetails.title,
                    filmReleaseDate = filmDetails.releaseDate,
                    filmId = filmDetails.id
                )
            )
        }

        return newList
    }
}