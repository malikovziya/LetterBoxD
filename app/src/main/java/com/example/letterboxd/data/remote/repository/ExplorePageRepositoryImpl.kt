package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.ExploreFilmItem
import com.example.letterboxd.domain.repository.ExplorePageRepository
import javax.inject.Inject

class ExplorePageRepositoryImpl @Inject constructor(val api : MyApi) : ExplorePageRepository {
    override suspend fun getExplorePageFilms(): List<ExploreFilmItem> {
        val result = api.getExplorePageFilms("a", 1)
        return result.results.map {
            ExploreFilmItem(
                image = it.posterPath,
                title = it.title
            )
        }
    }
}