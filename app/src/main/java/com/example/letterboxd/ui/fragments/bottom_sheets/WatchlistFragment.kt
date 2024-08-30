package com.example.letterboxd.ui.fragments.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.databinding.FragmentWatchlistBinding
import com.example.letterboxd.ui.adapters.left_navigation.WatchlistAdapter
import com.example.letterboxd.ui.view_models.bottom_sheets.WatchlistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class WatchlistFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentWatchlistBinding? = null
    val binding get() = _binding!!

    private val viewModel : WatchlistViewModel by viewModels()

    private var lastMovieId = -1
    private val adapter = WatchlistAdapter(goToMovie = {
        findNavController().navigate(WatchlistFragmentDirections.actionWatchlistFragmentToFilmDetailsFragment(it))
    }
    ){
        lifecycleScope.launch {
            viewModel.watchlistDao.deleteMovieById(it)
            viewModel.getWatchlistMovies()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.adapter = adapter

        observeViewModel()

        getLastAddedMovie()
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.watchlistMoviesToDisplay.collectLatest {
                adapter.updateAdapter(it)

                if (it.isEmpty()) binding.textViewMessage.visibility = View.VISIBLE
                else binding.textViewMessage.visibility = View.GONE
            }
        }
    }

    private fun getLastAddedMovie(){
        lifecycleScope.launch {
            val movieList = viewModel.watchlistDao.getWatchlist()
            lastMovieId = movieList.lastOrNull()?.movieId ?: -1
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getWatchlistMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        runBlocking {

            val ifExists = viewModel.watchlistDao.checkIfMovieAlreadyAdded(lastMovieId)

            val result = Bundle().apply {
                putString("resultKey", if (ifExists == null) "deleted" else "added")
            }

            setFragmentResult("bottomSheetRequestKey", result)
        }
    }

}