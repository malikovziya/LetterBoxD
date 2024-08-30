package com.example.letterboxd.data.local.watchlist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=[WatchlistEntity::class],version=1)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract val watchlistDao : WatchlistDao
}