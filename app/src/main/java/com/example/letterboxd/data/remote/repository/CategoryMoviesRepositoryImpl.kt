package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.common.utils.createCategoryFilms
import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.CategoryItem
import com.example.letterboxd.domain.repository.CategoryMoviesRepository
import javax.inject.Inject

class CategoryMoviesRepositoryImpl @Inject constructor(val api : MyApi) : CategoryMoviesRepository {
    override suspend fun getCategories(): List<CategoryItem> {
        val result = api.getNowPlayingFilms()

        val newList = createCategoryFilms(result.results)

        return newList
    }
}

