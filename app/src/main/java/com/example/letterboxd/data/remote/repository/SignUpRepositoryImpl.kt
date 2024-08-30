package com.example.letterboxd.data.remote.repository

import com.example.letterboxd.domain.repository.SignUpRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SignUpRepositoryImpl @Inject constructor(private val firestore : FirebaseFirestore): SignUpRepository{
    override suspend fun signUp(username: String, password: String, email : String): String {
        return suspendCancellableCoroutine { continuation ->
            val userData = hashMapOf(
                "username" to username,
                "email" to email,
                "password" to password // Storing password as plain text for simplicity
            )

            // Save the data under a collection called "users" with the document ID as the username
            firestore.collection("users").document(username)
                .set(userData)
                .addOnSuccessListener {
                    continuation.resume("Registered successfully")
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}

