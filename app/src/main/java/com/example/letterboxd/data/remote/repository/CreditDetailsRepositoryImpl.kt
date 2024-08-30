package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.CreditDetailsItem
import com.example.letterboxd.domain.repository.CreditDetailsRepository
import javax.inject.Inject

class CreditDetailsRepositoryImpl @Inject constructor(val api: MyApi) : CreditDetailsRepository {
    override suspend fun getCreditDetails(creditId: String): CreditDetailsItem {
        val response = api.getCreditDetails(creditId)
        return CreditDetailsItem(
            job = response.job,
            personId = response.person.id
        )
    }
}