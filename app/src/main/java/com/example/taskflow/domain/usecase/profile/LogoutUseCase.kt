package com.example.taskflow.domain.usecase.profile

import com.example.taskflow.domain.repository.ProfileRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    operator fun invoke(){
        repository.logout()
    }
}