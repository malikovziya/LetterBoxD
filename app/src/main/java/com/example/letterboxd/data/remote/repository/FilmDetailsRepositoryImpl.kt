package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.domain.model.DetailScreen
import com.example.letterboxd.domain.repository.FilmDetailsRepository
import javax.inject.Inject

class FilmDetailsRepositoryImpl @Inject constructor(val api : MyApi) : FilmDetailsRepository
{
    override suspend fun getFilmDetails(id: Int): DetailScreen {
        val response = api.getMovieDetails(id)
        response.popularity = (response.popularity.toString().split(".").first() +
                response.popularity.toString().split(".").last()).toDouble()
        return DetailScreen(
            filmName = response.title,
            filmDescription = response.overview,
            filmPosterPath = response.posterPath,
            filmBackgroundPath = response.backdropPath,
            filmRating = response.voteAverage.toFloat(),
            filmYear = response.releaseDate.split("-").first(),
            filmSlogan = response.tagline,
            filmDuration = response.runtime.toString() + " mins",
            filmViewCount = (if (response.popularity.toInt() < 1000) {
                    response.popularity.toString()
                }
                else if (response.popularity.toInt().div(1000) < 1000) {
                    response.popularity.toInt().div(1000).toString() + "k"
                }
                else {
                    response.popularity.toInt().div(1000000).toString() + "m"
            }),

            filmLikeCount = (if (response.revenue < 1000) {
                response.revenue.toString()
            }
            else if (response.revenue.div(1000) < 1000) {
                response.revenue.div(1000).toString() + "k"
            }
            else {
                response.revenue.div(1000000).toString() + "m"
            }),

            filmListCount = (if (response.voteCount < 1000) {
                response.voteCount.toString()
            }
            else if (response.voteCount.div(1000) < 1000) {
                response.voteCount.div(1000).toString() + "k"
            }
            else {
                response.voteCount.div(1000000).toString() + "m"
            }),
            filmDirector = response.productionCompanies[0].name,
            filmID = response.id
        )
    }
}