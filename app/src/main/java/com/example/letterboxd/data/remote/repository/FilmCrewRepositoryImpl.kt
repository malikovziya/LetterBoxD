package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.FilmCreditsItem
import com.example.letterboxd.domain.repository.FilmCrewRepository
import javax.inject.Inject

class FilmCrewRepositoryImpl @Inject constructor(val api : MyApi): FilmCrewRepository {
    override suspend fun getFilmCrew(id: Int): List<FilmCreditsItem> {
        val result = api.getFilmCredits(id)

        val newList = mutableListOf<FilmCreditsItem>()
        val stringList = mutableListOf<String>()

        result.crew.forEach{
            if ((it.profilePath != null) and !stringList.contains(it.profilePath)) {
                newList.add(FilmCreditsItem(
                    creditId = it.creditId,
                    imagePath = it.profilePath,
                    name = it.name,
                    gender = it.gender,
                    department = it.knownForDepartment,
                    popularity = it.popularity.toInt(),
                    character = null
                ))
                stringList.add(it.profilePath!!)
            }
        }
        return newList
    }
}