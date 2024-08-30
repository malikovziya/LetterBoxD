package com.example.letterboxd.ui.fragments.auth_fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.common.utils.hideKeyboard
import com.example.letterboxd.databinding.FragmentLoginBinding
import com.example.letterboxd.ui.activities.MainActivity
import com.example.letterboxd.ui.view_models.auth_viewmodels.LoginViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(baseFunction = FragmentLoginBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var myIntent: Intent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.buttonLogin.setOnClickListener {
            login()
        }

        observeViewModel()
    }

    private fun login(){
        val username = binding.usernameFieldLogin.text.toString()
        val password = binding.passwordFieldLogin.text.toString()

        viewModel.login(username, password)
        hideKeyboard(requireActivity(), binding.root)
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.clearFields.collectLatest {
                if (it){
                    binding.usernameFieldLogin.text = null
                    binding.passwordFieldLogin.text = null
                    binding.usernameFieldLogin.clearFocus()
                    binding.passwordFieldLogin.clearFocus()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collectLatest {
                binding.progressBar.isVisible = it
                binding.progressBar4.isVisible = it
                if (it) binding.buttonLogin.text = null
                else binding.buttonLogin.text = "Login"
            }
        }

        lifecycleScope.launch {
            viewModel.messagee.filterNotNull().collectLatest {
                Log.e("FRAGMENT LOG", it)
                FancyToast.makeText(requireContext(), it, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()

                if (it == "Login successful"){
                    myIntent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(myIntent)
                }
            }
        }
    }
}
