package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.ReviewDetailsItem

interface ReviewDetailsRepository {
    suspend fun getReviewDetails(id : String) : ReviewDetailsItem
}