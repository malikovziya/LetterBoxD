package com.example.letterboxd.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.common.utils.hideKeyboard
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsDao
import com.example.letterboxd.data.local.favourite_films.FavouriteFilmsEntity
import com.example.letterboxd.data.local.reviews.ReviewDao
import com.example.letterboxd.data.local.reviews.ReviewEntity
import com.example.letterboxd.databinding.FragmentAddReviewBinding
import com.example.letterboxd.ui.fragments.bottom_sheets.BottomSheetCalendarFragment
import com.example.letterboxd.ui.view_models.AddReviewViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddReviewFragment : BaseFragment<FragmentAddReviewBinding>(FragmentAddReviewBinding::inflate){
    private val args : AddReviewFragmentArgs by navArgs()

    val viewModel : AddReviewViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.movieName.text = args.filmName
        binding.movieYear.text = args.filmYear
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + args.filmPoster).into(binding.movieImage)

        val currentDate = getCurrentDate()
        binding.buttonOuter.text = currentDate

        lifecycleScope.launch {
            if (viewModel.favFilmsDao.determineIfFilmAlreadyInFavourites(args.id) != null){
                binding.heartButton.isSelected = true
            }
        }

        setButtonListeners(viewModel.favFilmsDao, args)
        observeViewModel()
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.loading.collectLatest {
                if (it){
                    binding.progressBar5.visibility = View.VISIBLE
                    binding.buttonPublish.text = null
                } else{
                    binding.progressBar5.visibility = View.GONE
                    binding.buttonPublish.text = "Publish"
                }
            }
        }
    }

    private fun setButtonListeners(favFilmsDao : FavouriteFilmsDao, args : AddReviewFragmentArgs){
        binding.heartButton.setOnClickListener {
            val heartButton = it as ImageButton
            heartButton.isSelected = !heartButton.isSelected
            if (heartButton.isSelected){
                lifecycleScope.launch {
                    if (favFilmsDao.determineIfFilmAlreadyInFavourites(args.id) == null){
                        favFilmsDao.insertFavouriteFilm(
                            FavouriteFilmsEntity(
                                id = args.id,
                                imagePathMovie = args.filmPoster,
                                nameMovie = args.filmName,
                            )
                        )
                    }
                }
                FancyToast.makeText(requireContext(), "You liked the movie -> ${args.filmName}", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
            } else{
                lifecycleScope.launch {
                    val film = favFilmsDao.determineIfFilmAlreadyInFavourites(args.id)
                    if (film != null) {
                        favFilmsDao.deleteFavouriteFilm(film)
                    }
                }
            }
        }

        binding.buttonInner.setOnClickListener {
            val calendar = BottomSheetCalendarFragment()
            calendar.setOnDateSelectedListener(object : OnDateSelectedListener{
                override fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
                    // Handle the selected date here
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    val selectedDate = sdf.format(calendar.time)

                    // Update UI or perform any other actions with the selected date
                    binding.buttonOuter.text = selectedDate
                }
            })
            calendar.show(parentFragmentManager, calendar.tag)
        }

        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.buttonPublish.setOnClickListener {
            hideKeyboard(requireActivity(), binding.root)
            binding.editText.clearFocus()

            val reviewText = binding.editText.text.toString()
            val rating = binding.scaleRatingBar.rating
            val reviewDate = binding.buttonOuter.text.toString()
            val filmId = args.id

            if (reviewText == "") {
                FancyToast.makeText(requireContext(), "Please write your review", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                return@setOnClickListener
            }
            else if (rating == 0f){
                FancyToast.makeText(requireContext(), "Please give your rating", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                return@setOnClickListener

            } else {
                lifecycleScope.launch {
                    viewModel.publishReview(requireContext(), binding, reviewText, rating, reviewDate, filmId)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate() : String{
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val currentDate = sdf.format(Date())
        return currentDate
    }
}