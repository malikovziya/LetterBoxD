package com.example.letterboxd.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.databinding.FragmentReviewDetailsBinding
import com.example.letterboxd.domain.repository.ReviewDetailsRepository
import com.example.letterboxd.ui.view_models.ReviewDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReviewDetailsFragment : BaseFragment<FragmentReviewDetailsBinding>(FragmentReviewDetailsBinding::inflate) {
    private val args : ReviewDetailsFragmentArgs by navArgs()
    private val viewModel: ReviewDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()

        setListeners()
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.reviewDetails.collectLatest { result ->
                with(binding){
                    if (result != null) {
                        textView39.text = result.username
                        textView49.text = result.mediaType
                        textView50.text = result.mediaName
                        if (result.rating.toInt() == 0) {
                            textView46.text = "N/A"
                            ratingBar5.visibility = View.GONE
                        } else {
                            textView46.text = result.rating.div(2).toString()
                            ratingBar5.rating = result.rating.div(2)
                        }
                        textView52.text = result.createdTime
                        textLink.text = result.reviewLink
                        textView55.text = result.updatedTime
                        textFulllComment.text = result.review
                        if (args.reviewDetails.profileImage == null){
                            imageView10.setImageResource(R.drawable.anonymous_user)
                        } else {
                            Picasso.get().load("https://image.tmdb.org/t/p/w500${args.reviewDetails.profileImage}")
                                .into(imageView10)
                        }
                        if (args.reviewDetails.movieImage == ""){
                            Picasso.get().load("https://image.tmdb.org/t/p/w500${viewModel.sharedPrefs.getString("background_path", null)}")
                                .into(imageView16)
                        } else {
                            Picasso.get().load("https://image.tmdb.org/t/p/w500${args.reviewDetails.movieImage}")
                                .into(imageView16)
                        }
                    }
                }
            }
        }
    }

    private fun setListeners(){
        binding.textLink.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.textLink.text.toString()))
            startActivity(intent)
        }

        binding.buttonReturn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getReviewDetails(args.reviewDetails.reviewId)
    }
}