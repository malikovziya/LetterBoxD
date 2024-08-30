package com.example.letterboxd.data.local.favourite_films

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouriteFilmsEntity::class], version = 1)
abstract class FavouriteFilmsDatabase : RoomDatabase() {
    abstract val favFilmsDao : FavouriteFilmsDao
}
