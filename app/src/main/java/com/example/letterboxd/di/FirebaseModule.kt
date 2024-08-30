package com.example.letterboxd.di

import com.example.letterboxd.data.remote.repository.LoginRepositoryImpl
import com.example.letterboxd.data.remote.repository.SignUpRepositoryImpl
import com.example.letterboxd.domain.repository.LoginRepository
import com.example.letterboxd.domain.repository.SignUpRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Singleton
    @Provides
    fun getFirebaseAuth() : FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun getFirebaseFirestore() : FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun getLoginRepository(firestore : FirebaseFirestore) : LoginRepository {
        return LoginRepositoryImpl(firestore)
    }

    @Singleton
    @Provides
    fun getSignUpRepository(firestore : FirebaseFirestore) : SignUpRepository {
        return SignUpRepositoryImpl(firestore)
    }
}