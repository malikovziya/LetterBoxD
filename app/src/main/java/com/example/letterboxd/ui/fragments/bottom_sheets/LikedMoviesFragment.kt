package com.example.letterboxd.ui.fragments.bottom_sheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.R
import com.example.letterboxd.databinding.FragmentLikedMoviesBinding
import com.example.letterboxd.domain.model.FavouriteFilmItem
import com.example.letterboxd.ui.adapters.left_navigation.LikedMoviesAdapter
import com.example.letterboxd.ui.adapters.profile_page.FavouriteFilmsAdapter
import com.example.letterboxd.ui.view_models.bottom_sheets.LikedMoviesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LikedMoviesFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentLikedMoviesBinding? = null
    val binding get() = _binding!!

    private val viewModel : LikedMoviesViewModel by viewModels()

    val adapter = LikedMoviesAdapter(
        function = {
            findNavController().navigate(LikedMoviesFragmentDirections.actionLikedMoviesFragmentToFilmDetailsFragment(it.movieId))
        }
    ){
        viewModel.removeLikedMovie(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView5.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.filmsToDisplay.collectLatest {
                adapter.updateAdapter(it)

                if (it.isEmpty()) binding.textViewMessage2.visibility = View.VISIBLE
                else binding.textViewMessage2.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getLikedMovies()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}