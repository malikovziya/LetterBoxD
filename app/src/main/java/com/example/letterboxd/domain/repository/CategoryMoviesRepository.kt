package com.example.letterboxd.domain.repository

import com.example.letterboxd.domain.model.CategoryItem

interface CategoryMoviesRepository {
    suspend fun getCategories(): List<CategoryItem>
}