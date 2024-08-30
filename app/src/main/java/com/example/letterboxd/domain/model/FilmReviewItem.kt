package com.example.letterboxd.domain.model

class FilmReviewItem (
    val reviewId : String,
    val imagePath : String?,
    val reviewMakerName : String,
    val givenRating : Double?,
    val creationTime : String,
    val comment : String
)