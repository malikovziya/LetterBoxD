package com.example.letterboxd.domain.repository

interface LoginRepository {
    suspend fun login(username : String, password : String) : String
}