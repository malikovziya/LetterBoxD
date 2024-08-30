package com.example.letterboxd.domain.repository

interface SignUpRepository  {
    suspend fun signUp(username : String, password : String, email : String) : String
}