package com.example.taskflow.domain.usecase.profile

import com.example.taskflow.domain.repository.ProfileRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(password : String) : Result<Unit>{
        return repository.changePassword(password)
    }
}