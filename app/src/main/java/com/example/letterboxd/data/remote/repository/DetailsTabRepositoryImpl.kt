package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.DetailsTabItem
import com.example.letterboxd.domain.repository.DetailsTabRepository
import javax.inject.Inject

class DetailsTabRepositoryImpl @Inject constructor(
    val api : MyApi
) : DetailsTabRepository {
    override suspend fun getDetailsTabData(id : Int): DetailsTabItem {
        val result = api.getMovieDetails(id)

        val textCountries = mutableListOf<String>()
        result.productionCountries.forEach {
            textCountries.add(it.name)
        }

        val textCompanies = mutableListOf<String>()
        result.productionCompanies.forEach {
            textCompanies.add(it.name)
        }

        val textLanguages = mutableListOf<String>()
        result.spokenLanguages.forEach {
            textLanguages.add(it.englishName)
        }

        val textGenres = mutableListOf<String>()
        result.genres.forEach {
            textGenres.add(it.name)
        }

        return DetailsTabItem(
            textCountries,
            textCompanies,
            textLanguages,
            textGenres,
            revenue = result.revenue,
            budget = result.budget,
            releaseDate = result.releaseDate,
            textLink = result.homepage,
        )
    }
}