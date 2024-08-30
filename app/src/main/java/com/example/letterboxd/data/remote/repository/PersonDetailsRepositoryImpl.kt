package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.PersonDetailsItem
import com.example.letterboxd.domain.repository.PersonDetailsRepository
import javax.inject.Inject

class PersonDetailsRepositoryImpl @Inject constructor(
    val api : MyApi
): PersonDetailsRepository {
    override suspend fun getPersonDetails(id : Int): PersonDetailsItem {
        val response = api.getPersonDetails(id)

        val personDetails = PersonDetailsItem(
            birthday = response.birthday,
            placeOfBirth = response.placeOfBirth,
            deathDate = response.deathday,
            biography = response.biography,
            homepage = response.homepage
        )

        return personDetails
    }
}