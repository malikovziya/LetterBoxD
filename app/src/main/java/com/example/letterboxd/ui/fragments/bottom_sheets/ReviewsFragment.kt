package com.example.letterboxd.ui.fragments.bottom_sheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.databinding.FragmentReviewsBinding
import com.example.letterboxd.ui.adapters.left_navigation.ReviewsAdapter
import com.example.letterboxd.ui.fragments.HomepageFragmentDirections
import com.example.letterboxd.ui.view_models.bottom_sheets.ReviewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewsFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentReviewsBinding? = null
    val binding get() = _binding!!

    private val viewModel : ReviewsViewModel by viewModels()

    private val adapterForReviews = ReviewsAdapter(null, null){
        findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToFilmDetailsFragment(it))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapterForReviews

        observeViewModel()

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.reviewsToDisplay.collectLatest {
                adapterForReviews.updateAdapter(it)

                if (it.isEmpty()) binding.textViewMessage4.visibility = View.VISIBLE
                else binding.textViewMessage4.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.profilePhoto.collectLatest {
                adapterForReviews.updatePhotos(it)
            }
        }

        lifecycleScope.launch {
            viewModel.usernameToDisplay.collectLatest {
                adapterForReviews.updateReviewMaker(it)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        viewModel.getReviews()
        viewModel.getUser()
        viewModel.getProfilePhoto(requireContext())
    }
}