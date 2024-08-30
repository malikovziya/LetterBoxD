package com.example.letterboxd.ui.fragments.auth_fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.common.utils.hideKeyboard
import com.example.letterboxd.databinding.FragmentSignUpBinding
import com.example.letterboxd.ui.view_models.auth_viewmodels.SignUpViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(baseFunction = FragmentSignUpBinding::inflate) {
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonSignUp.setOnClickListener{
            signUp()
        }

        binding.textLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        observeViewModel()
    }

    private fun signUp(){
        val username = binding.usernameField.text.toString()
        val password = binding.passwordField.text.toString()
        val email = binding.emailField.text.toString()

        viewModel.signUp(username, password, email)
        hideKeyboard(requireActivity(), binding.root)
    }


    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.clearFields.collectLatest {
                if (it){
                    binding.usernameField.text = null
                    binding.passwordField.text = null
                    binding.emailField.text = null
                    binding.usernameField.clearFocus()
                    binding.passwordField.clearFocus()
                    binding.emailField.clearFocus()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collectLatest {
                binding.progressBar2.isVisible = it
                binding.progressBar3.isVisible = it
                if (it) binding.buttonSignUp.text = null
                else binding.buttonSignUp.text = "Sign Up"
            }
        }

        lifecycleScope.launch {
            viewModel.message.filterNotNull().collectLatest {
                FancyToast.makeText(requireContext(), it, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
            }
        }
    }
}