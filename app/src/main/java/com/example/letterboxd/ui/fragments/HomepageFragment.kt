package com.example.letterboxd.ui.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.common.utils.getRotationDegrees
import com.example.letterboxd.common.utils.rotateBitmap
import com.example.letterboxd.databinding.FragmentHomepageBinding
import com.example.letterboxd.domain.model.ReviewDetails
import com.example.letterboxd.ui.activities.AuthActivity
import com.example.letterboxd.ui.activities.MainActivity
import com.example.letterboxd.ui.adapters.home_page.HomePageAdapter
import com.example.letterboxd.ui.adapters.home_page.HomePageCategoriesAdapter
import com.example.letterboxd.ui.adapters.home_page.HomePageReviewsAdapter
import com.example.letterboxd.ui.fragments.bottom_sheets.ReviewsFragment
import com.example.letterboxd.ui.view_models.HomepageViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class HomepageFragment : BaseFragment<FragmentHomepageBinding>(FragmentHomepageBinding::inflate) {
    private val viewModel : HomepageViewModel by viewModels()

    private val adapterPopularMovies = HomePageAdapter {
        findNavController().navigate(
            HomepageFragmentDirections.actionHomepageFragmentToFilmDetailsFragment(
                id = it.id
            )
        )
    }

    private val adapterCategoryMovies = HomePageCategoriesAdapter(
        onClick = {
            findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToMovieListsFragment(
                fourMoviePaths = it
            ))
        }
    )

    private val adapterFriendsReview = HomePageReviewsAdapter (
        goToReview = {
            findNavController().navigate(
                HomepageFragmentDirections.actionHomepageFragmentToReviewDetailsFragment(
                    reviewDetails = ReviewDetails(
                        movieImage = it.movieImage,
                        profileImage = it.profileImage,
                        reviewId = it.reviewId
                    )
                )
            )
        }
    ){
        findNavController().navigate(
            HomepageFragmentDirections.actionHomepageFragmentToFilmDetailsFragment(
                id = it
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nameFromSharedPref = viewModel.sharedPrefName.getString("name", viewModel.sharedPrefUsername.getString("username", "Guest user"))
        binding.textView10.text = nameFromSharedPref

        binding.rvPopular.adapter = adapterPopularMovies
        binding.rvComment.adapter = adapterFriendsReview
        binding.rvCategory.adapter = adapterCategoryMovies

        observeAdapters()

        val headerView = requireActivity().findViewById<View>(R.id.header_view)
        provideProfileImages(headerView)
        provideHeaderViewNames(headerView)

        setListeners()

        binding.shimmerLayout.startShimmer()
        binding.shimmerLayoutCategory.startShimmer()
        binding.shimmerLayoutComment.startShimmer()
    }

    private fun setListeners(){
        binding.buttonSideNav.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.imageView.setOnClickListener {
            findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToProfileFragment())
        }

        val group1 = requireActivity().findViewById<NavigationView>(R.id.nv_group1)
        val group2 = requireActivity().findViewById<NavigationView>(R.id.nv_group2)

        group1.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item101 -> {
                    FancyToast.makeText(requireContext(), "You are in homepage", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                }
                R.id.item102 -> {
                    findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToWatchedMoviesFragment())
                }
                R.id.item103 -> {
                    val bottomSheet = ReviewsFragment()
                    bottomSheet.show(parentFragmentManager, "ReviewsFragment")
//                    findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToReviewsFragment())
                }
                R.id.item104 -> {
                    findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToWatchlistFragment())
                }
                R.id.item105 -> {
                    findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToLikedMoviesFragment())
                }
                else -> {
                    FancyToast.makeText(requireContext(), "pressed ${it.itemId}", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
                }
            }
            true
        }
        group2.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item106 -> {
                    Firebase.auth.signOut().also {
                        lifecycleScope.launch {
                            viewModel.loading.update { true }
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                            delay(1000)
                            viewModel.sharedPrefUsername.edit().putString("username", null).apply()
                            val intent = Intent(requireActivity(), AuthActivity::class.java)
                            intent.putExtra("SHOW_TOAST", true)
                            startActivity(intent)
                            viewModel.loading.update { false }
                        }
                    }

                }
                else -> {
                    FancyToast.makeText(requireContext(), "pressed ${it.itemId}", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
                }
            }
            true
        }
    }

    private fun observeAdapters(){
        lifecycleScope.launch {
            viewModel.popularMoviesToDisplay.collect{
                adapterPopularMovies.updateAdapter(it)

                delay(1000)
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.INVISIBLE
                binding.rvPopular.visibility = View.VISIBLE

                resetConstraints1()
            }
        }

        lifecycleScope.launch {
            viewModel.friendsReviewToDisplay.collect{
                adapterFriendsReview.updateAdapter(it)

                delay(3000)

                binding.shimmerLayoutComment.stopShimmer()
                binding.shimmerLayoutComment.visibility = View.INVISIBLE
                binding.rvComment.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.categoriesToDisplay.collect{
                adapterCategoryMovies.updateAdapter(it)

                delay(1000)
                binding.shimmerLayoutCategory.stopShimmer()
                binding.shimmerLayoutCategory.visibility = View.INVISIBLE
                binding.rvCategory.visibility = View.VISIBLE

                resetConstraints2()
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collectLatest {
                binding.progressBar8.isVisible = it
            }
        }
    }

    private fun provideProfileImages(headerView : View){
        val imageProfile = headerView.findViewById<ImageView>(R.id.imagePp)

        val imagePath = viewModel.sharedPref.getString("profile_picture_path", null)
        if (imagePath != null) {
            val imageFile = File(requireContext().filesDir, imagePath)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            val rotationDegrees = getRotationDegrees(imageFile.absolutePath)
            val rotatedBitmap = rotateBitmap(bitmap, rotationDegrees)

            binding.imageView.setImageBitmap(rotatedBitmap)
            imageProfile.setImageBitmap(rotatedBitmap)
        } else {
            imageProfile.setImageResource(R.drawable.anonymous_user)
            binding.imageView.setImageResource(R.drawable.anonymous_user)
        }
    }

    private fun provideHeaderViewNames(headerView : View){
        val nameView = headerView.findViewById<TextView>(R.id.textView4)
        val usernameView = headerView.findViewById<TextView>(R.id.textView6)

        val usernameFromSharedPref = viewModel.sharedPrefUsername.getString("username", "Guest user")
        val nameToDisplay = viewModel.sharedPrefName.getString("name", usernameFromSharedPref)

        nameView.text = nameToDisplay
        usernameView.text = "@${usernameFromSharedPref}"
    }

    private fun resetConstraints1(){
        val params = binding.textView13.layoutParams as ConstraintLayout.LayoutParams

        params.topToBottom = R.id.rvPopular
        params.marginStart = 16

        binding.textView13.layoutParams = params
    }

    private fun resetConstraints2(){
        val params = binding.textView15.layoutParams as ConstraintLayout.LayoutParams

        params.topToBottom = R.id.rvCategory
        params.marginStart = 0

        binding.textView15.layoutParams = params
    }
}