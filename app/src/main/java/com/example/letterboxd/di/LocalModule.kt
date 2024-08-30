package com.example.letterboxd.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDatabase
import com.example.letterboxd.data.local.recent_watched_films.RecentWatchedFilmsDao
import com.example.letterboxd.data.local.recent_watched_films.RecentWatchedFilmsDatabase
import com.example.letterboxd.data.local.reviews.ReviewDao
import com.example.letterboxd.data.local.reviews.ReviewDatabase
import com.example.letterboxd.data.local.watchlist.WatchlistDao
import com.example.letterboxd.data.local.watchlist.WatchlistDatabase
import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.data.remote.repository.FavouriteFilmRepositoryImpl
import com.example.letterboxd.data.remote.repository.LikedMoviesRepositoryImpl
import com.example.letterboxd.data.remote.repository.RecentWatchedFilmRepositoryImpl
import com.example.letterboxd.data.remote.repository.ReviewRepositoryImpl
import com.example.letterboxd.domain.repository.FavouriteFilmRepository
import com.example.letterboxd.domain.repository.LikedMoviesRepository
import com.example.letterboxd.domain.repository.RecentWatchedFilmRepository
import com.example.letterboxd.domain.repository.ReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideFavouriteFilmsDatabase(application : Application) : FavouriteFilmsDatabase {
        return Room.databaseBuilder(application, FavouriteFilmsDatabase::class.java, "favourite_movies_db")
            .build()
    }

    @Provides
    @Singleton
    fun provideFavouriteFilmsDao(database : FavouriteFilmsDatabase) : FavouriteFilmsDao {
        return database.favFilmsDao
    }

    @Provides
    @Singleton
    fun provideRecentWatchedFilmsDatabase(application: Application) : RecentWatchedFilmsDatabase {
        return Room.databaseBuilder(application, RecentWatchedFilmsDatabase::class.java, "watched_films_database").build()
    }


    @Provides
    @Singleton
    fun provideRecentWatchedFilmsDao(database : RecentWatchedFilmsDatabase) : RecentWatchedFilmsDao {
        return database.recentWatchedFilmsDao
    }

    @Provides
    @Singleton
    fun provideReviewsDatabase(application: Application) : ReviewDatabase {
        return Room.databaseBuilder(application, ReviewDatabase::class.java, "review_database").build()
    }

    @Provides
    @Singleton
    fun provideReviewsDao(database : ReviewDatabase) : ReviewDao {
        return database.reviewDao
    }

    @Provides
    @Singleton
    fun provideWatchlistDatabase(application: Application) : WatchlistDatabase {
        return Room.databaseBuilder(application, WatchlistDatabase::class.java, "watchlist_database").build()
    }

    @Provides
    @Singleton
    fun provideWatchlistDao(database : WatchlistDatabase) : WatchlistDao {
        return database.watchlistDao
    }

    @Provides
    @Singleton
    fun getFavouriteFilmsRepository(dao : FavouriteFilmsDao) : FavouriteFilmRepository {
        return FavouriteFilmRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun getRecentWatchedFilmsRepository(api : MyApi, database : RecentWatchedFilmsDatabase) : RecentWatchedFilmRepository {
        return RecentWatchedFilmRepositoryImpl(api, database)
    }

    @Provides
    @Singleton
    fun getReviewsRepository(api : MyApi, database : ReviewDatabase) : ReviewRepository {
        return ReviewRepositoryImpl(api, database)
    }

    @Provides
    @Singleton
    fun getLikedMoviesRepository(dao : FavouriteFilmsDao) : LikedMoviesRepository {
        return LikedMoviesRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    @Named("profile_picture")
    fun getProfilePictureSharedPreferences(application : Application) : SharedPreferences {
        return application.getSharedPreferences("letterboxd", Application.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    @Named("profile_background")
    fun getProfileBackgroundSharedPreferences(application : Application) : SharedPreferences {
        return application.getSharedPreferences("profileBackground", Application.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    @Named("review_details")
    fun getReviewDetailsSharedPreferences(application : Application) : SharedPreferences {
        return application.getSharedPreferences("reviewDetails", Application.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    @Named("username")
    fun getUsernameSharedPreferences(application : Application) : SharedPreferences {
        return application.getSharedPreferences("username", Application.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    @Named("name")
    fun getNameSharedPreferences(application : Application) : SharedPreferences {
        return application.getSharedPreferences("name", Application.MODE_PRIVATE)
    }
}