package com.example.letterboxd.ui.view_models

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.domain.model.CategoryItem
import com.example.letterboxd.domain.model.FriendsReviewItem
import com.example.letterboxd.domain.model.PopularFilmItem
import com.example.letterboxd.domain.repository.CategoryMoviesRepository
import com.example.letterboxd.domain.repository.HomepageReviewsRepository
import com.example.letterboxd.domain.repository.PopularFilmRepository
import com.example.letterboxd.ui.adapters.home_page.HomePageAdapter
import com.example.letterboxd.ui.adapters.home_page.HomePageCategoriesAdapter
import com.example.letterboxd.ui.adapters.home_page.HomePageReviewsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val repository : PopularFilmRepository,
    private val repository2 : HomepageReviewsRepository,
    private val repository3 : CategoryMoviesRepository,
    @Named("profile_picture") val sharedPref : SharedPreferences,
    @Named("username") val sharedPrefUsername : SharedPreferences,
    @Named("name") val sharedPrefName : SharedPreferences
) : ViewModel() {

    val popularMoviesToDisplay = MutableStateFlow<List<PopularFilmItem>>(emptyList())
    val friendsReviewToDisplay = MutableStateFlow<List<FriendsReviewItem>>(emptyList())
    val categoriesToDisplay = MutableStateFlow<List<CategoryItem>>(emptyList())

    val loading = MutableStateFlow(false)

    init {
        getPopularMoviesToDisplay()
        getFriendsReview()
        getCategories()
    }

    private fun getPopularMoviesToDisplay(){
        viewModelScope.launch {
            val response = repository.getPopularFilms()
            popularMoviesToDisplay.emit(response)
        }
    }

    private fun getFriendsReview(){
        viewModelScope.launch {
            val response = repository2.getFriendsReview()
            friendsReviewToDisplay.emit(response)
        }
    }

    private fun getCategories(){
        viewModelScope.launch {
            val response = repository3.getCategories()
            categoriesToDisplay.emit(response)
        }
    }
}