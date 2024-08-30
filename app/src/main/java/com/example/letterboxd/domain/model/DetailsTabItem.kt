package com.example.letterboxd.domain.model

class DetailsTabItem (
    val textCountries : List<String>,
    val textCompanies : List<String>,
    val textLanguages : List<String>,
    val textGenres : List<String>,
    val revenue : Long,
    val budget : Int,
    val releaseDate : String,
    val textLink : String?
)