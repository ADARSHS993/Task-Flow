package com.example.taskflow.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun getCurrentUser() = firebaseAuth.currentUser

    suspend fun updateProfile(
        name : String
    ): Result<Unit> {

        return try {
            val user = firebaseAuth.currentUser?:
            return Result.failure(Exception("User is not logged in"))

            val request = userProfileChangeRequest {
                displayName = name
            }

            user.updateProfile(request).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(
        newPassword: String
    ) : Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User is not logged in"))

            user.updatePassword(newPassword).await()
            Result.success(Unit)
        }catch (e : Exception){
            Result.failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}