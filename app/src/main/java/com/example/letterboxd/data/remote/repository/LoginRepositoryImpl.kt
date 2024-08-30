package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LoginRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore
) : LoginRepository {
    override suspend fun login(username: String, password: String): String {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection("users").document(username)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val storedPassword = document.getString("password")
                        if (storedPassword == password) {
                            continuation.resume("Login successful")
                        } else {
                            continuation.resume("Incorrect password")
                        }
                    } else {
                        continuation.resume("User does not exist")
                    }
                }
                .addOnFailureListener {
                    continuation.resume(it.toString())
                }
        }
    }
}