package com.example.letterboxd.domain.model

data class FriendsReviewItem(
    val idMovie : Int,
    val profilePath : String?,
    val moviePicturePath : String,
    val movieBackgroundPath : String,
    var filmName : String,
    val year : String,
    val reviewMaker : String,
    val rating : Float?,
    val comment : String,
    val reviewDate : String,
    val reviewId : String,
)