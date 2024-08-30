package com.example.letterboxd.common.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun getAllUsernames(firestore: FirebaseFirestore): List<String> {
    return suspendCancellableCoroutine { continuation ->
        firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val usernames = mutableListOf<String>()
                for (document in result) {
                    val username = document.getString("username")
                    if (username != null) {
                        usernames.add(username)
                    }
                }
                continuation.resume(usernames)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}

fun isValidEmail(email: String): Boolean {
    // Regular expression pattern for basic email validation
    val emailPattern = Regex(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )

    // Check if email matches the pattern
    if (!email.matches(emailPattern)) {
        return false
    }

    // Check for invalid characters in local part and domain part
    val invalidCharsPattern = Regex("[,;:\\[\\]()<>\"]")
    if (invalidCharsPattern.containsMatchIn(email)) {
        return false
    }

    // Check if the local part and domain part do not start or end with invalid characters
    val parts = email.split("@")
    if (parts.size != 2) {
        return false
    }

    val localPart = parts[0]
    val domainPart = parts[1]

    // Check if the local part and domain part start or end with invalid characters
    if (localPart.startsWith(".") || localPart.endsWith(".") || domainPart.startsWith(".") || domainPart.endsWith(".")) {
        return false
    }

    // Check for consecutive dots in the local part
    if (localPart.contains("..")) {
        return false
    }

    // Check if domain part contains invalid characters
    val domainInvalidCharsPattern = Regex("[^a-zA-Z0-9.-]")
    if (domainInvalidCharsPattern.containsMatchIn(domainPart)) {
        return false
    }

    // Check if domain part contains consecutive dots
    if (domainPart.contains("..")) {
        return false
    }

    return true
}
