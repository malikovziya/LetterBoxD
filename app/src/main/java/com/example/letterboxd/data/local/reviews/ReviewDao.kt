package com.example.letterboxd.data.local.reviews

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewDao {
    @Insert
    suspend fun insertReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query("SELECT * FROM ReviewEntity")
    suspend fun getAllReviews(): List<ReviewEntity>

    @Query("Delete from ReviewEntity")
    suspend fun deleteAllReviews()

    @Query("SELECT * FROM ReviewEntity WHERE filmId = :id")
    suspend fun getReviewById(id: Int): ReviewEntity?

}