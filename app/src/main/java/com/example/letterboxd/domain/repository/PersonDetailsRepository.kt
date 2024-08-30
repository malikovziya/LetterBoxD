package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.PersonDetailsItem

interface PersonDetailsRepository {
    suspend fun getPersonDetails(id : Int) : PersonDetailsItem
}