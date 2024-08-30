package com.example.letterboxd.data.local.watchlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WatchlistDao {
    @Insert
    suspend fun insertMovie(movie : WatchlistEntity)

    @Delete
    suspend fun deleteMovie(movie : WatchlistEntity)

    @Query("DELETE FROM WatchlistEntity WHERE movieId = :movieId")
    suspend fun deleteMovieById(movieId : Int)

    @Query("SELECT * FROM WatchlistEntity")
    suspend fun getWatchlist(): List<WatchlistEntity>

    @Query("SELECT * FROM WatchlistEntity WHERE movieId = :movieId")
    suspend fun checkIfMovieAlreadyAdded(movieId : Int) : WatchlistEntity?

    @Query("DELETE FROM WatchlistEntity")
    suspend fun clearWatchlist()
}