package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.DetailsTabItem

interface DetailsTabRepository {
    suspend fun getDetailsTabData(id : Int) : DetailsTabItem
}