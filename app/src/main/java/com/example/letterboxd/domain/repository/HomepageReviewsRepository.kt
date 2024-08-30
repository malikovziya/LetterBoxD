package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.FriendsReviewItem

interface HomepageReviewsRepository {
    suspend fun getFriendsReview() : List<FriendsReviewItem>
}