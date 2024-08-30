package com.example.letterboxd.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.common.utils.roundFloats
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsEntity
import com.example.letterboxd.data.local.watchlist.WatchlistEntity
import com.example.letterboxd.databinding.FragmentFilmDetailsBinding
import com.example.letterboxd.domain.model.DetailScreen
import com.example.letterboxd.domain.model.ReviewDetails
import com.example.letterboxd.ui.adapters.details_page.DetailTabAdapter
import com.example.letterboxd.ui.adapters.details_page.FilmCastsAdapter
import com.example.letterboxd.ui.adapters.details_page.FilmCrewsAdapter
import com.example.letterboxd.ui.adapters.details_page.FilmReviewsAdapter
import com.example.letterboxd.ui.view_models.FilmDetailsViewModel
import com.google.android.material.tabs.TabLayout
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmDetailsFragment : BaseFragment<FragmentFilmDetailsBinding>(FragmentFilmDetailsBinding::inflate) {
    val fragArgs: FilmDetailsFragmentArgs by navArgs()

    private val viewModel : FilmDetailsViewModel by viewModels()

    private val adapterForCasts = FilmCastsAdapter {
        findNavController().navigate(FilmDetailsFragmentDirections.actionFilmDetailsFragmentToCreditDetailsFragment(
            creditDetails = it
        ))
    }

    private val adapterForCrews = FilmCrewsAdapter {
        findNavController().navigate(FilmDetailsFragmentDirections.actionFilmDetailsFragmentToCreditDetailsFragment(
            creditDetails = it
        ))
    }

    private val adapterForDetailTabRecyclerView1 = DetailTabAdapter()
    private val adapterForDetailTabRecyclerView2 = DetailTabAdapter()
    private val adapterForDetailTabRecyclerView3 = DetailTabAdapter()
    private val adapterForDetailTabRecyclerView4 = DetailTabAdapter()

    private lateinit var adapterForReviews : FilmReviewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapterForReviews = FilmReviewsAdapter(goToReview = {
                findNavController().navigate(
                    FilmDetailsFragmentDirections.actionFilmDetailsFragmentToReviewDetailsFragment(
                        ReviewDetails(
                            movieImage = it.movieImage,
                            profileImage = it.profileImage,
                            reviewId = it.reviewId
                        )
                    )
                )
            }
        )

        binding.recyclerViewCasts.adapter = adapterForCasts
        binding.recyclerViewCrews.adapter = adapterForCrews
        binding.rvReviews.adapter = adapterForReviews

        binding.recyclerView4.adapter = adapterForDetailTabRecyclerView1
        binding.recyclerView6.adapter = adapterForDetailTabRecyclerView2
        binding.recyclerView7.adapter = adapterForDetailTabRecyclerView3
        binding.recyclerView8.adapter = adapterForDetailTabRecyclerView4

        observeViewModel()

        setGraphics()

        setAddToWatchlistButton()

        setWatchedButton()

        lifecycleScope.launch {
            val response = viewModel.repository.getFilmDetails(fragArgs.id)
            with(binding) {
                filmName.text = response.filmName
                filmYear.text = response.filmYear
                filmDuration.text = response.filmDuration
                filmDirector.text = response.filmDirector
                filmViewCount.text = response.filmViewCount
                filmLikeCount.text = response.filmLikeCount
                filmListCount.text = response.filmListCount
                filmSlogan.text = response.filmSlogan
                filmOverview.text = response.filmDescription
                filmRating.text = roundFloats(response.filmRating.div(2), 1).toString()
                ratingBar3.rating = response.filmRating / 2
                Picasso.get().load("https://image.tmdb.org/t/p/w500${response.filmPosterPath}")
                    .into(posterImage)
                if (response.filmBackgroundPath != null) Picasso.get().load("https://image.tmdb.org/t/p/w500${response.filmBackgroundPath}")
                    .into(backgroundImage)
                else backgroundImage.setImageResource(R.drawable.img_7)

                viewModel.sharedPref.edit().putString("background_path", response.filmBackgroundPath).apply()

                if (viewModel.recentWatchedFilmsDao.returnRecentFilmById(response.filmID) != null){
                    buttonWatched.isSelected = true
                }
            }

            setTabItemListeners()
            setListeners(response)
        }
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.filmCasts.collectLatest {
                adapterForCasts.updateAdapter(it)
            }
        }

        lifecycleScope.launch {
            viewModel.filmCrews.collectLatest {
                adapterForCrews.updateAdapter(it)
            }
        }

        lifecycleScope.launch {
            viewModel.detailsTabData.collectLatest {
                if (it != null){
                    binding.textView65.text = it.releaseDate
                    if (it.budget == 0) binding.textView66.text = "Unknown"
                    else binding.textView66.text = "$" + it.budget
                    if (it.revenue == 0L) binding.textView67.text = "Unknown"
                    else binding.textView67.text = "$" + it.revenue

                    if (it.textLink.isNullOrEmpty()) {
                        binding.textView70.visibility = View.GONE
                        binding.textView71.visibility = View.GONE
                    }
                    else binding.textView71.text = it.textLink

                    adapterForDetailTabRecyclerView1.updateAdapter(it.textCountries)
                    adapterForDetailTabRecyclerView2.updateAdapter(it.textCompanies)
                    adapterForDetailTabRecyclerView3.updateAdapter(it.textLanguages)
                    adapterForDetailTabRecyclerView4.updateAdapter(it.textGenres)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.filmReviews.collectLatest {
                adapterForReviews.updateAdapter(it)
                if (it.size <= 3) {
                    binding.textSeeAll.visibility = View.GONE
                } else {
                    binding.textSeeAll.visibility = View.VISIBLE
                }

                if (it.isEmpty()) binding.textView45.visibility = View.VISIBLE
                else binding.textView45.visibility = View.GONE
            }
        }
    }

    private fun setListeners(response : DetailScreen) {
        binding.textSeeAll.setOnClickListener{
            if (binding.textSeeAll.text == "See All") {
                adapterForReviews.showAllReviews()
                binding.textSeeAll.text = "See Less"
            } else {
                adapterForReviews.seeLessReviews()
                binding.textSeeAll.text = "See All"
            }
        }

        binding.buttonRate.setOnClickListener {
            findNavController().navigate(
                FilmDetailsFragmentDirections.actionFilmDetailsFragmentToAddReviewFragment(
                    filmName = response.filmName,
                    filmYear = response.filmYear,
                    filmPoster = response.filmPosterPath,
                    id = response.filmID
                )
            )
        }

        binding.buttonWatched.setOnClickListener {
            val watchedButton = it as ImageButton
            watchedButton.isSelected = !watchedButton.isSelected

            if (watchedButton.isSelected){
                findNavController().navigate(FilmDetailsFragmentDirections.actionFilmDetailsFragmentToRateWatchedFilmFragment(
                    newId = response.filmID,
                    filmName = response.filmName
                ))
            } else{
                lifecycleScope.launch {
                    val film = viewModel.recentWatchedFilmsDao.returnRecentFilmById(response.filmID)
                    if (film != null) {
                        viewModel.recentWatchedFilmsDao.deleteWatchedFilm(film)
                        FancyToast.makeText(requireContext(), "${response.filmName} is removed from Recent Watched!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                    }
                }
            }
        }

        binding.buttonAddWatchlist.setOnClickListener {
            if (binding.buttonAddWatchlist.text == "Add to Watchlist"){
                binding.buttonAddWatchlist.text = "Added to Watchlist"
                binding.buttonAddWatchlist.textSize = 11f
                binding.buttonAddWatchlist.setIconResource(R.drawable.watched)
                lifecycleScope.launch {
                    viewModel.watchlistDao.insertMovie(
                        WatchlistEntity(
                            moviePath = response.filmPosterPath,
                            movieId = response.filmID,
                            movieName = response.filmName,
                            movieDuration = response.filmDuration,
                            movieRating = response.filmRating
                        )
                    )
                    findNavController().navigate(FilmDetailsFragmentDirections.actionFilmDetailsFragmentToWatchlistFragment())
                }
            } else {
                binding.buttonAddWatchlist.text = "Add to Watchlist"
                binding.buttonAddWatchlist.textSize = 12f
                binding.buttonAddWatchlist.setIconResource(R.drawable.add_to_watchlist)
                lifecycleScope.launch {
                    viewModel.watchlistDao.deleteMovieById(
                        response.filmID
                    )
                }
            }
        }

        binding.button3.setOnClickListener {
            if (binding.button3.text == "Add to Favourites"){
                binding.button3.text = "Favourited"
                binding.button3.setIconResource(R.drawable.watched)
                lifecycleScope.launch {
                    viewModel.likedMoviesDao.insertFavouriteFilm(
                        FavouriteFilmsEntity(
                            id = fragArgs.id,
                            imagePathMovie = response.filmPosterPath,
                            nameMovie = response.filmName
                        )
                    )
                }
                FancyToast.makeText(requireContext(), "${response.filmName} is added to Favourites!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
            } else {
                binding.button3.text = "Add to Favourites"
                binding.button3.setIconResource(R.drawable.add_to_list)
                lifecycleScope.launch {
                    val film = viewModel.likedMoviesDao.determineIfFilmAlreadyInFavourites(fragArgs.id)
                    if (film != null) viewModel.likedMoviesDao.deleteFavouriteFilm(film)
                }
            }
        }

        binding.buttonGoBackk.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setTabItemListeners(){
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        binding.recyclerViewCrews.visibility = View.GONE
                        binding.cl.visibility = View.GONE
                        binding.recyclerViewCasts.visibility = View.VISIBLE
                        lifecycleScope.launch {
                            val response = viewModel.repository2.getFilmCasts(fragArgs.id)
                            adapterForCasts.updateAdapter(response)
                        }
                    }
                    1 -> {
                        binding.recyclerViewCasts.visibility = View.GONE
                        binding.cl.visibility = View.GONE
                        binding.recyclerViewCrews.visibility = View.VISIBLE
                        lifecycleScope.launch {
                            val response = viewModel.repository3.getFilmCrew(fragArgs.id)
                            adapterForCrews.updateAdapter(response)
                        }
                    }
                    2 -> {
                        binding.recyclerViewCasts.visibility = View.GONE
                        binding.recyclerViewCrews.visibility = View.GONE
                        binding.cl.visibility = View.VISIBLE
                        lifecycleScope.launch {
                            viewModel.getDetailsTabData(fragArgs.id)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun setGraphics(){
        lifecycleScope.launch {
            if (viewModel.watchlistDao.checkIfMovieAlreadyAdded(fragArgs.id) != null){
                binding.buttonAddWatchlist.text = "Added to Watchlist"
                binding.buttonAddWatchlist.textSize = 11f
                binding.buttonAddWatchlist.setIconResource(R.drawable.watched)
            }
        }

        lifecycleScope.launch {
            if (viewModel.likedMoviesDao.determineIfFilmAlreadyInFavourites(fragArgs.id) != null){
                binding.button3.text = "Favourited"
                binding.button3.setIconResource(R.drawable.watched)
            }
        }
    }

    private fun setAddToWatchlistButton(){
        setFragmentResultListener("bottomSheetRequestKey") { _, bundle ->
            val result = bundle.getString("resultKey")
            if (result == "deleted"){
                binding.buttonAddWatchlist.text = "Add to Watchlist"
                binding.buttonAddWatchlist.textSize = 12f
                binding.buttonAddWatchlist.setIconResource(R.drawable.add_to_watchlist)
            }
        }
    }

    private fun setWatchedButton(){
        setFragmentResultListener("bottomSheetRequestKey2") { _, bundle ->
            val result = bundle.getString("resultKey2")
            if (result == "false"){
                binding.buttonWatched.isSelected = false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        setGraphics()
        viewModel.getFilmCrew(fragArgs.id)
        viewModel.getFilmCasts(fragArgs.id)
        viewModel.getDetailsTabData(fragArgs.id)
        viewModel.getFilmReviews(fragArgs.id)
    }
}