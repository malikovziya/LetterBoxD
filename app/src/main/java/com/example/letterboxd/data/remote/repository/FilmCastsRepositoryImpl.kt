package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.FilmCreditsItem
import com.example.letterboxd.domain.repository.FilmCastsRepository
import javax.inject.Inject

class FilmCastsRepositoryImpl @Inject constructor(val api : MyApi) : FilmCastsRepository {
    override suspend fun getFilmCasts(id: Int): List<FilmCreditsItem> {
        val response = api.getFilmCredits(id)

        val newList = mutableListOf<FilmCreditsItem>()
        val stringList = mutableListOf<String>()

        response.cast.forEach {
            if ((it.profilePath != null) and !(stringList.contains(it.profilePath))) {
                newList.add(FilmCreditsItem(
                    creditId = it.creditId,
                    imagePath = it.profilePath,
                    name = it.name,
                    character = it.character,
                    gender = it.gender,
                    department = it.knownForDepartment,
                    popularity = it.popularity.toInt()
                ))
                stringList.add(it.profilePath!!)
            }
        }
        return newList
    }
}