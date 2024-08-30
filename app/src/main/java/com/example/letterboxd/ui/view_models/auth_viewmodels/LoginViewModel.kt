package com.example.letterboxd.ui.view_models.auth_viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxd.common.utils.getAllUsernames
import com.example.letterboxd.domain.repository.LoginRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository : LoginRepository,
    @Named("username") val sharedPref : SharedPreferences
) : ViewModel() {
    val loading = MutableStateFlow(false)
    val messagee = MutableStateFlow<String?>(null)
    val clearFields = MutableStateFlow(false)

    fun login(username : String, password : String){
        viewModelScope.launch {
            loading.update { true }
            messagee.update { null }
            clearFields.update { false }

            val allUsers = getAllUsernames(Firebase.firestore)

            if (username.isEmpty()){
                messagee.update {
                    "Please enter your username"
                }
            }
            else if (password.isEmpty()){
                messagee.update {
                    "Please enter your password"
                }
            }
            else if (!allUsers.contains(username)){
                messagee.update {
                    "User does not exist"
                }
            }
            else{
                val result = loginRepository.login(username, password)
                if (result == "Login successful") {
                    sharedPref.edit().putString("username", username).apply()
                }
                messagee.update {
                    result
                }
            }

            loading.update { false }
            clearFields.update { true }
        }
    }
}