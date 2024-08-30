package com.example.letterboxd.data.local.favourite_films

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.letterboxd.domain.model.DetailScreen

@Entity
data class FavouriteFilmsEntity (
    @PrimaryKey(autoGenerate = true) val idFavourite : Int = 0,
    val id : Int,
    val imagePathMovie : String,
    val nameMovie : String,
)