package com.example.letterboxd.ui.view_models.auth_viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.common.utils.countOccurrences
import com.example.letterboxd.common.utils.getAllUsernames
import com.example.letterboxd.common.utils.isValidEmail
import com.example.letterboxd.domain.repository.SignUpRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val repository: SignUpRepository
) : ViewModel() {
    val loading = MutableStateFlow(false)
    val message = MutableStateFlow<String?>(null)
    val clearFields = MutableStateFlow(false)

    fun signUp(username : String, password : String, email : String){
        viewModelScope.launch {
            loading.update { true }
            clearFields.update { false }
            message.update { null }

            val allUsers = getAllUsernames(Firebase.firestore)

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()){
                message.update {
                    "All fields must be filled"
                }
            }
            else if (allUsers.contains(username)){
                message.update {
                    "User already exists"
                }
            }
            else if (!isValidEmail(email)){
                message.update {
                    "Email is badly formatted"
                }
            }
            else if (username.length < 6){
                message.update {
                    "Username must be at least 6 characters long"
                }
            }
            else if (password.length < 6){
                message.update {
                    "Password must be at least 6 characters long"
                }
            }
            else {
                message.update {
                    repository.signUp(username, password, email)
                }
            }

            loading.update { false }
            clearFields.update { true }
        }
    }
}
