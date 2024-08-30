package com.example.letterboxd.ui.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.common.utils.getRotationDegrees
import com.example.letterboxd.common.utils.rotateBitmap
import com.example.letterboxd.databinding.FragmentExploreBinding
import com.example.letterboxd.ui.adapters.explore_page.ExplorePageAdapterPager
import com.example.letterboxd.ui.view_models.ExploreFragmentViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>(FragmentExploreBinding::inflate) {
    private val viewModel: ExploreFragmentViewModel by viewModels()
    private val adapter = ExplorePageAdapterPager {
        findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToFilmDetailsFragment(id = it.id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.adapter = adapter

        setProfilePicture()

        lifecycleScope.launch {
            viewModel.pagedData.collectLatest {
                adapter.submitData(it)
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Update the search query in ViewModel
                FancyToast.makeText(requireContext(), "submitted", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                viewModel.setSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally update search query as text changes
                viewModel.setSearchQuery(newText)
                return true
            }
        })

        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() // If you only want the first checked chip id

            val genre = when (checkedId) {
                R.id.chipAction -> "Action"
                R.id.chipComedy -> "Comedy"
                R.id.chipFamily -> "Family"
                R.id.chipHorror -> "Horror"
                R.id.chipAdventure -> "Adventure"
                R.id.chipAnimation -> "Animation"
                R.id.chipSciFi -> "Science Fiction"
                R.id.chipThriller -> "Thriller"
                R.id.chipDrama -> "Drama"
                R.id.chipRomance -> "Romance"
                R.id.chipMystery -> "Mystery"
                R.id.chipFantasy -> "Fantasy"
                R.id.chipCrime -> "Crime"
                R.id.chipWar -> "War"
                R.id.chipHistory -> "History"
                R.id.chipDocumentary -> "Documentary"
                R.id.chipWestern -> "Western"
                R.id.chipMusic -> "Music"
                R.id.chipTvMovie -> "TV Movie"

                else -> null
            }

            if (genre != null) {
                FancyToast.makeText(requireContext(), "Genre selected: $genre", FancyToast.LENGTH_SHORT, FancyToast.INFO, false)
                    .show()
                viewModel.setGenreFilter(genre)
            } else {
                viewModel.setGenreFilter(null)
            }
        }

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.frameLayout.visibility = View.VISIBLE
            } else {
                binding.frameLayout.visibility = View.GONE
                viewModel.setGenreFilter(null)
            }
        }
    }

    private fun setProfilePicture() {
        val profilePicture = viewModel.sharedPrefsProfilePicture.getString("profile_picture_path", null)
        if (profilePicture != null) {
            val imageFile = File(requireContext().filesDir, profilePicture)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            val rotationDegrees = getRotationDegrees(imageFile.absolutePath)
            val rotatedBitmap = rotateBitmap(bitmap, rotationDegrees)

            binding.imageView19.setImageBitmap(rotatedBitmap)
        } else {
            binding.imageView19.setImageResource(R.drawable.anonymous_user)
        }
    }
}
