package com.example.letterboxd.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val image1: String,
    val image2: String,
    val image3: String,
    val image4: String,
    val id1: Int,
    val id2: Int,
    val id3: Int,
    val id4: Int
) : Parcelable