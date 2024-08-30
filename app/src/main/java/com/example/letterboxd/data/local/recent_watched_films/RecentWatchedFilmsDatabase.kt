package com.example.letterboxd.data.local.recent_watched_films

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentWatchedFilmsEntity::class], version = 1)
abstract class RecentWatchedFilmsDatabase : RoomDatabase(){
    abstract val recentWatchedFilmsDao: RecentWatchedFilmsDao
}