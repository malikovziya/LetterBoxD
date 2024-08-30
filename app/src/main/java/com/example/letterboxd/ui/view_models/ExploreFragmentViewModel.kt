package com.example.letterboxd.ui.view_models

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.data.remote.model.Result
import com.example.letterboxd.data.remote.paging.MyPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ExploreFragmentViewModel @Inject constructor(
    private val apiService: MyApi,
    @Named("profile_picture") val sharedPrefsProfilePicture : SharedPreferences
) : ViewModel() {
    private val searchQuery = MutableStateFlow<String?>(null)
    private val genreFilter = MutableStateFlow<String?>(null)

    fun setSearchQuery(query: String?) {
        searchQuery.value = query
    }

    fun setGenreFilter(query: String?) {
        genreFilter.value = query
    }

    val pagedData: Flow<PagingData<Result>> = combine(searchQuery, genreFilter) { query, genre ->
        Pair(query, genre)
    }.flatMapLatest { (query, genre) ->
        Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { MyPagingSource(apiService, query, genre, "fa22bdcd3f461d2aec53c6b2b47e85e7") }
        ).flow
    }.cachedIn(viewModelScope)
}
