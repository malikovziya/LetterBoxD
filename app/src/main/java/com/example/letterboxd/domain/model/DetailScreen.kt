package com.example.letterboxd.domain.model

class DetailScreen(
    val filmName: String,
    val filmPosterPath: String,
    val filmBackgroundPath : String?,
    val filmYear : String,
    val filmDuration : String,
    val filmDirector : String,
    val filmRating : Float,
    val filmDescription : String?,
    val filmSlogan : String,
    val filmViewCount : String,
    val filmLikeCount : String,
    val filmListCount : String,
    val filmID : Int
)