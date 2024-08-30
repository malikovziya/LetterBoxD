package com.example.letterboxd.ui.fragments.bottom_sheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.R
import com.example.letterboxd.databinding.FragmentWatchedMoviesBinding
import com.example.letterboxd.ui.adapters.left_navigation.WatchedFilmsAdapter
import com.example.letterboxd.ui.adapters.profile_page.RecentWatchedAdapter
import com.example.letterboxd.ui.view_models.bottom_sheets.WatchedMoviesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchedMoviesFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentWatchedMoviesBinding? = null
    val binding get() = _binding!!

    private val viewModel : WatchedMoviesViewModel by viewModels()

    val adapter = WatchedFilmsAdapter(
        function = {
            findNavController().navigate(WatchedMoviesFragmentDirections.actionWatchedMoviesFragmentToFilmDetailsFragment(it.filmId))
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchedMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.watchedMoviesToDisplay.collectLatest {
                adapter.updateAdapter(it)

                if (it.isEmpty()) binding.textViewMessage3.visibility = View.VISIBLE
                else binding.textViewMessage3.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getWatchedMovies()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}