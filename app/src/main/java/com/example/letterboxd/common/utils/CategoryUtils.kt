package com.example.letterboxd.common.utils

import com.example.letterboxd.data.remote.model.OneMovie
import com.example.letterboxd.domain.model.CategoryItem

fun createCategoryFilms(results: List<OneMovie>): List<CategoryItem> {
    // Extract poster paths and movie IDs
    val posterPaths = results.map { it.posterPath }
    val ids = results.map { it.id }

    // Group poster paths and IDs into chunks of 4
    val groupedPaths = posterPaths.chunked(4)
    val groupedIds = ids.chunked(4)

    // Create CategoryFilm instances from each group of 4
    return groupedPaths.zip(groupedIds).map { (paths, ids) ->
        // Pad paths and IDs with empty strings and zeroes if necessary
        val paddedPaths = paths + List(4 - paths.size) { "" }
        val paddedIds = ids + List(4 - ids.size) { 0 }

        val (image1, image2, image3, image4) = paddedPaths
        val (id1, id2, id3, id4) = paddedIds

        CategoryItem(image1, image2, image3, image4, id1, id2, id3, id4)
    }
}