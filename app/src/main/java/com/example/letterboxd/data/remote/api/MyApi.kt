package com.example.letterboxd.data.remote.api

import com.example.letterboxd.data.remote.model.CreditDetailsResponse
import com.example.letterboxd.data.remote.model.FilmCreditsResponse
import com.example.letterboxd.data.remote.model.FilmDetailsResponse
import com.example.letterboxd.data.remote.model.FilmReviewsResponse
import com.example.letterboxd.data.remote.model.NowPlayingMoviesResponse
import com.example.letterboxd.data.remote.model.PersonDetailsResponse
import com.example.letterboxd.data.remote.model.PopularFilmsResponse
import com.example.letterboxd.data.remote.model.ReviewDetailsResponse
import com.example.letterboxd.data.remote.model.TopRatedMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {
    @GET("movie/popular?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getPopularFilms(): PopularFilmsResponse

    @GET("movie/top_rated?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getTopRatedFilms(): TopRatedMoviesResponse

    @GET("movie/now_playing?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getNowPlayingFilms(): NowPlayingMoviesResponse

    @GET("movie/{movie_id}?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): FilmDetailsResponse

    @GET("movie/{movie_id}/credits?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getFilmCredits(
        @Path("movie_id") movieId: Int
    ): FilmCreditsResponse

    @GET("movie/{movie_id}/reviews?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getFilmReviews(
        @Path("movie_id") movieId: Int
    ): FilmReviewsResponse

    @GET("review/{review_id}?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getReviewDetails(
        @Path("review_id") reviewId : String
    ) : ReviewDetailsResponse

    @GET("discover/movie")
    suspend fun getExplorePageFilms(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): PopularFilmsResponse

    @GET("search/movie")
    suspend fun searchFilms(
        @Query("api_key") apiKey: String,
        @Query("query") movieName: String,
        @Query("page") page: Int
    ): PopularFilmsResponse

    @GET("credit/{movie_id}?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getCreditDetails(
        @Path("movie_id") movieId: String
    ) : CreditDetailsResponse

    @GET("person/{person_id}?api_key=fa22bdcd3f461d2aec53c6b2b47e85e7")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int
    ) : PersonDetailsResponse
}