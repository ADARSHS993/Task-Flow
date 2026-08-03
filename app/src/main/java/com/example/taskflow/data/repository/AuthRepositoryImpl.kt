package com.example.taskflow.data.repository

import com.example.taskflow.domain.model.User
import com.example.taskflow.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): Result<FirebaseUser?> {

        return try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(email,password)
                .await()

            Result.success(result.user)
        } catch (e : Exception){
            Result.failure(
                e
            )
        }
    }

    override suspend fun register(
        email: String,
        password: String,
    ): Result<FirebaseUser?> {

        return try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(email,password)
                .await()

            Result.success(result.user)
        } catch (e : Exception){
            Result.failure(e)
        }
    }

    override fun logout() {
         firebaseAuth.signOut()
    }



}