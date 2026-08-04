package com.example.taskflow.data.repository

import com.example.taskflow.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun isEmailVerified(): Boolean {
        return firebaseAuth.currentUser?.isEmailVerified == true
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            
            val user = result.user
            if (user != null && fullName.isNotBlank()) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = fullName
                }
                user.updateProfile(profileUpdates).await()
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.sendEmailVerification().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No authenticated user found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reloadUser(): Result<Boolean> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.reload().await()
                Result.success(user.isEmailVerified)
            } else {
                Result.failure(Exception("No authenticated user found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}