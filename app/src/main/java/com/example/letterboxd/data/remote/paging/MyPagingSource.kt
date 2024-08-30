package com.example.letterboxd.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.letterboxd.common.utils.convertIntegersToStrings
import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.data.remote.model.Result

class MyPagingSource(
    private val apiService: MyApi,
    private val searchQuery: String? = null,
    private val genre : String? = null,
    private val apiKey : String
) : PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val position = params.key ?: 1
            val response = if (searchQuery.isNullOrEmpty()) {
                apiService.getExplorePageFilms(apiKey, position)
            } else {
                apiService.searchFilms(apiKey, searchQuery, position)
            }

            val filteredResults = genre?.let {
                response.results.filter { convertIntegersToStrings(it.genreIds).contains(genre) }
            } ?: response.results

            Log.e("RESULTS", filteredResults.toString())
            LoadResult.Page(
                data = filteredResults,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.totalPages) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

