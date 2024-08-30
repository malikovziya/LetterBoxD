package com.example.letterboxd.data.local.recent_watched_films

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentWatchedFilmsEntity (
    @PrimaryKey(autoGenerate = true) val idRow : Int = 0,
    val idFilm : Int,
    val rating : Float,
    val dateWatched : String
)