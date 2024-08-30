package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.ReviewDetailsItem
import com.example.letterboxd.domain.repository.ReviewDetailsRepository

class ReviewDetailsRepositoryImpl(val api : MyApi) : ReviewDetailsRepository {
    override suspend fun getReviewDetails(id: String): ReviewDetailsItem {
        val result = api.getReviewDetails(id)
        val newData = ReviewDetailsItem(
            username = result.author,
            mediaType = result.mediaType,
            mediaName = result.mediaTitle,
            rating = result.authorDetails.rating.toFloat(),
            createdTime = result.createdAt,
            updatedTime = result.updatedAt,
            review = result.content,
            reviewLink = result.url
        )
        return newData
    }
}