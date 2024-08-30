package com.example.letterboxd.data.local.watchlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class WatchlistEntity (
    @PrimaryKey(autoGenerate = true) val watchlistId : Int = 0,
    val moviePath : String,
    val movieName : String,
    val movieRating : Float,
    val movieDuration : String,
    val movieId : Int
)