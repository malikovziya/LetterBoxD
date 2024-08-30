package com.example.letterboxd.ui.fragments.auth_fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.databinding.FragmentOnBoardingBinding
import com.example.letterboxd.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {
    @Inject
    @Named("username") lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }


        lifecycleScope.launch {
            delay(3000)
            val usernameToCheck = sharedPref.getString("username", null)
            if (usernameToCheck != null) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}