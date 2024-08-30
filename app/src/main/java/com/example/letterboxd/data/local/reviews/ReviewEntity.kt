package com.example.letterboxd.data.local.reviews

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReviewEntity (
    @PrimaryKey(autoGenerate = true) val reviewId : Int = 0,
    val filmId : Int,
    val comment : String,
    val rating : Float,
    val reviewDate : String
)