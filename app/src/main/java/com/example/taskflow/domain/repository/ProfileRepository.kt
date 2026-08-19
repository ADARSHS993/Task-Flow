package com.example.taskflow.domain.repository

import com.example.taskflow.domain.model.UserProfile

interface ProfileRepository {
    fun getCurrent(): UserProfile?

    suspend fun updateProfile(
        name : String
    ) : Result<Unit>

    suspend fun changePassword(
        newPassword : String
    ): Result<Unit>

    fun logout()
}