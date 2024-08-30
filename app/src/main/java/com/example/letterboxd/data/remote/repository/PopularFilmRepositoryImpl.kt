package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.PopularFilmItem
import com.example.letterboxd.domain.repository.PopularFilmRepository
import javax.inject.Inject

class PopularFilmRepositoryImpl @Inject constructor(private val api : MyApi) : PopularFilmRepository {
    override suspend fun getPopularFilms(): List<PopularFilmItem> {
        val resultFromApi = api.getPopularFilms()
        return resultFromApi.results.map {
            PopularFilmItem(
                urlPath = it.posterPath,
                id = it.id,
            )
        }
    }
}