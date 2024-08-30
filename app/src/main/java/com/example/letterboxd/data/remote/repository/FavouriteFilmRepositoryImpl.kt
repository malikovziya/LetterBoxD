package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDatabase
import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.FavouriteFilmItem
import com.example.letterboxd.domain.repository.FavouriteFilmRepository
import javax.inject.Inject

class FavouriteFilmRepositoryImpl @Inject constructor(
    private val dao : FavouriteFilmsDao
) : FavouriteFilmRepository {
    override suspend fun getFavouriteFilms(): List<FavouriteFilmItem> {
        val response = dao.getAllFavouriteFilms()

        val newList = mutableListOf<FavouriteFilmItem>()
        response.forEach {
            newList.add(
                FavouriteFilmItem(
                    filmImage = it.imagePathMovie,
                    id = it.id
                )
            )
        }
        return newList
    }
}