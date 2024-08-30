package com.example.letterboxd.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FilmCreditsItem (
    val creditId : String,
    val imagePath : String?,
    val character : String?,
    val department : String,
    val popularity : Int,
    val name : String,
    val gender : Int,
) : Parcelable