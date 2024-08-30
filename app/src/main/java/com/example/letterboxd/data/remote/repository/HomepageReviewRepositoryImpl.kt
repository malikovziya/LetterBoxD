package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.FriendsReviewItem
import com.example.letterboxd.domain.repository.HomepageReviewsRepository
import javax.inject.Inject

class HomepageReviewRepositoryImpl @Inject constructor(val api : MyApi) : HomepageReviewsRepository {
    override suspend fun getFriendsReview(): List<FriendsReviewItem> {
        val response = api.getTopRatedFilms()

        val resultList = mutableListOf<FriendsReviewItem>()

        response.results.forEach {
            val reviews = api.getFilmReviews(it.id)
            if (reviews.results.isNotEmpty()){
                resultList.add(
                    FriendsReviewItem(
                        profilePath = reviews.results.first().authorDetails.avatarPath,
                        moviePicturePath = it.posterPath,
                        filmName = it.originalTitle,
                        year = it.releaseDate,
                        reviewMaker = reviews.results.first().author,
                        reviewDate = reviews.results.first().createdAt,
                        comment = reviews.results.first().content,
                        rating = reviews.results.first().authorDetails.rating?.toFloat(),
                        idMovie = it.id,
                        movieBackgroundPath = it.backdropPath,
                        reviewId = reviews.results.first().id
                    )
                )
            }
        }
        return resultList
    }
}