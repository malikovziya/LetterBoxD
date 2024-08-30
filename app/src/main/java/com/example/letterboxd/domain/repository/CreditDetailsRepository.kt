package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.CreditDetailsItem

interface CreditDetailsRepository {
    suspend fun getCreditDetails(creditId : String) : CreditDetailsItem
}