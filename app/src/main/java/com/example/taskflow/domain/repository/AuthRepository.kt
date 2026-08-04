package com.example.taskflow.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    val currentUser: FirebaseUser?

    fun isUserLoggedIn(): Boolean

    fun isEmailVerified(): Boolean

    suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser?>

    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<FirebaseUser?>

    suspend fun sendPasswordResetEmail(
        email: String
    ): Result<Unit>

    suspend fun sendEmailVerification(): Result<Unit>

    suspend fun reloadUser(): Result<Boolean>

    fun logout()
}