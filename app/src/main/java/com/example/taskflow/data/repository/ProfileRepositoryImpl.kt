package com.example.taskflow.data.repository

import com.example.taskflow.data.mapper.toUserProfile
import com.example.taskflow.data.remote.ProfileRemoteDataSource
import com.example.taskflow.domain.model.UserProfile
import com.example.taskflow.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource
): ProfileRepository {
    override fun getCurrent(): UserProfile? {
        return remoteDataSource.getCurrentUser()?.toUserProfile()
    }

    override suspend fun updateProfile(name: String): Result<Unit> {
        return remoteDataSource.updateProfile(name)
    }

    override suspend fun changePassword(newPassword: String): Result<Unit> {
       return remoteDataSource.changePassword(newPassword)
    }

    override fun logout() {
        remoteDataSource.logout()
    }
    }
