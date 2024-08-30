package com.example.letterboxd.ui.fragments.bottom_sheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.letterboxd.databinding.FragmentCreditDetailsBinding
import com.example.letterboxd.ui.view_models.bottom_sheets.CreditDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreditDetailsFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentCreditDetailsBinding? = null
    val binding get() = _binding!!

    private val args : CreditDetailsFragmentArgs by navArgs()
    private val viewModel : CreditDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load("https://image.tmdb.org/t/p/w500${args.creditDetails.imagePath}")
            .into(binding.imagePerson)
        binding.textDepartment.text = args.creditDetails.department
        binding.textPersonName.text = args.creditDetails.name
        binding.textPersonPopularity.text = args.creditDetails.popularity.toString()
        binding.textPersonGender.text = args.creditDetails.gender.toString()
        when (binding.textPersonGender.text) {
            "1" -> binding.textPersonGender.text = "Female"
            "2" -> binding.textPersonGender.text = "Male"
            else -> binding.textPersonGender.text = "Not Given"
        }
        if (args.creditDetails.character == null) {
            binding.textCharacter.visibility = View.GONE
            binding.textView62.visibility = View.GONE
        } else binding.textCharacter.text = args.creditDetails.character

        observeViewModel()
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.creditDetailsToDisplay.collectLatest {
                binding.textJob.text = it?.job ?: "NOT GIVEN"
            }
        }

        lifecycleScope.launch {
            viewModel.personDetailsToDisplay.collectLatest {
                if (it != null) {
                    Log.e("BIO", "${it.biography.toString()}" )
                    if (it.biography == "") {
                        binding.textView61.text = "N/A"
                    } else {
                        binding.textView61.text = it.biography
                    }

                    if (it.homepage == null) {
                        binding.textView72.visibility = View.GONE
                        binding.textView73.visibility = View.GONE
                    }
                    else binding.textView73.text = it.homepage.toString()

                    if (it.birthday == null && it.placeOfBirth == null) binding.textBirthday.text = "N/A"
                    else if (it.birthday == null) binding.textBirthday.text = it.placeOfBirth
                    else if (it.placeOfBirth == null) binding.textBirthday.text = it.birthday
                    else binding.textBirthday.text = "${it.birthday}, ${it.placeOfBirth}"

                    if (it.deathDate != null) {
                        binding.textDied.text = it.deathDate.toString()
                    } else {
                        binding.textDied.text = "-"
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCreditDetails(args.creditDetails.creditId)
    }
}