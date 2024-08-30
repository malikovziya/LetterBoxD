package com.example.letterboxd.data.local.reviews

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReviewEntity::class], version = 1)
abstract class ReviewDatabase : RoomDatabase() {
    abstract val reviewDao: ReviewDao
}