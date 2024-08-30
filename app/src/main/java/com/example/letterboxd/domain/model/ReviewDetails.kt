package com.example.letterboxd.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ReviewDetails(
    val movieImage : String,
    val profileImage : String?,
    val reviewId : String
) : Parcelable
