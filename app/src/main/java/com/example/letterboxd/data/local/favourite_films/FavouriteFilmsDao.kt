package com.example.letterboxd.data.local.favourite_films

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteFilmsDao {
    @Insert
    suspend fun insertFavouriteFilm(film : FavouriteFilmsEntity)

    @Delete
    suspend fun deleteFavouriteFilm(film : FavouriteFilmsEntity)

    @Query("SELECT * FROM FavouriteFilmsEntity")
    suspend fun getAllFavouriteFilms() : List<FavouriteFilmsEntity>

    @Query("SELECT * FROM FavouriteFilmsEntity WHERE id = :id")
    suspend fun determineIfFilmAlreadyInFavourites(id : Int) : FavouriteFilmsEntity?

    @Query("DELETE FROM FavouriteFilmsEntity")
    suspend fun deleteAllFavouriteFilms()
}