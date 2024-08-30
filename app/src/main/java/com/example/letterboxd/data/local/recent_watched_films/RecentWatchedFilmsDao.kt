package com.example.letterboxd.data.local.recent_watched_films

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentWatchedFilmsDao {
    @Insert
    suspend fun insertWatchedFilm(film : RecentWatchedFilmsEntity)

    @Delete
    suspend fun deleteWatchedFilm(film : RecentWatchedFilmsEntity)

    @Query("SELECT * FROM RecentWatchedFilmsEntity")
    suspend fun getAllRecentWatchedFilms() : List<RecentWatchedFilmsEntity>

    @Query("DELETE FROM RecentWatchedFilmsEntity")
    suspend fun deleteAllRecentWatchedFilms()

    @Query("SELECT * FROM RecentWatchedFilmsEntity WHERE idFilm = :id")
    suspend fun returnRecentFilmById(id : Int) : RecentWatchedFilmsEntity?

}