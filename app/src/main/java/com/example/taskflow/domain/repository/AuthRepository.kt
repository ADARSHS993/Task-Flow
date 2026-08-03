package com.example.taskflow.domain.repository

import com.example.taskflow.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser?>

    suspend fun register(
        email: String,
        password: String
    ): Result<FirebaseUser?>

    fun logout()

}