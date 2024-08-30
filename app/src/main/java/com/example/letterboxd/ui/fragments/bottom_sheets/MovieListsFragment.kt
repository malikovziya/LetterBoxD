package com.example.letterboxd.ui.fragments.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.letterboxd.R
import com.example.letterboxd.databinding.FragmentMovieListsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class MovieListsFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentMovieListsBinding? = null
    val binding get() = _binding!!
    private val args : MovieListsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImages()

        setListeners()
    }

    private fun loadImages(){
        Picasso.get().load("https://image.tmdb.org/t/p/w500${args.fourMoviePaths.image1}").into(binding.imageMovie1)
        Picasso.get().load("https://image.tmdb.org/t/p/w500${args.fourMoviePaths.image2}").into(binding.imageMovie2)
        Picasso.get().load("https://image.tmdb.org/t/p/w500${args.fourMoviePaths.image3}").into(binding.imageMovie3)
        Picasso.get().load("https://image.tmdb.org/t/p/w500${args.fourMoviePaths.image4}").into(binding.imageMovie4)
    }

    private fun setListeners(){
        binding.imageMovie1.setOnClickListener {
            findNavController().navigate(MovieListsFragmentDirections.actionMovieListsFragmentToFilmDetailsFragment(args.fourMoviePaths.id1))
        }
        binding.imageMovie2.setOnClickListener {
            findNavController().navigate(MovieListsFragmentDirections.actionMovieListsFragmentToFilmDetailsFragment(args.fourMoviePaths.id2))
        }
        binding.imageMovie3.setOnClickListener {
            findNavController().navigate(MovieListsFragmentDirections.actionMovieListsFragmentToFilmDetailsFragment(args.fourMoviePaths.id3))
        }
        binding.imageMovie4.setOnClickListener {
            findNavController().navigate(MovieListsFragmentDirections.actionMovieListsFragmentToFilmDetailsFragment(args.fourMoviePaths.id4))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}