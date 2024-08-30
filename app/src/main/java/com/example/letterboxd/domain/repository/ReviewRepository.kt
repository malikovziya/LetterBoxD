package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.ReviewItem

interface ReviewRepository {
    suspend fun getReviews(): List<ReviewItem>
}