package com.example.letterboxd.ui.fragments.bottom_sheets

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.letterboxd.data.local.recent_watched_films.RecentWatchedFilmsDao
import com.example.letterboxd.data.local.recent_watched_films.RecentWatchedFilmsEntity
import com.example.letterboxd.databinding.FragmentRateWatchedFilmBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RateWatchedFilmFragment : BottomSheetDialogFragment()  {
    private val args : RateWatchedFilmFragmentArgs by navArgs()

    @Inject
    lateinit var recentWatchedFilmsDao : RecentWatchedFilmsDao

    private var _binding : FragmentRateWatchedFilmBinding? = null
    val binding get() = _binding!!

    private var submitted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRateWatchedFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textDate.text = getFormattedDate()

        setListeners()

    }

    private fun setListeners() {
        binding.buttonAdjust.setOnClickListener {
            showDatePickerDialog(binding.textDate)
        }

        binding.buttonSubmit.setOnClickListener {
            val rating = binding.scaleRatingBar.rating
            val dateWatched = binding.textDate.text.toString()

            lifecycleScope.launch {
                recentWatchedFilmsDao.insertWatchedFilm(
                    RecentWatchedFilmsEntity(
                        idFilm = args.newId,
                        rating = rating,
                        dateWatched = dateWatched
                    )
                )
                FancyToast.makeText(requireContext(), "${args.filmName} is added to watched films!!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                submitted = true
            }
            dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFormattedDate() : String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
        val formattedDate = currentDate.format(formatter)

        return formattedDate
    }

    private fun showDatePickerDialog(tvSelectedDate : TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                tvSelectedDate.text = formattedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        val result = Bundle().apply {
            putString("resultKey2", submitted.toString())
        }

        setFragmentResult("bottomSheetRequestKey2", result)
    }
}
