package com.example.taskflow.presentation.profile

import com.example.taskflow.domain.model.UserProfile

data class ProfileUiState(
    val profile : UserProfile? = null,
    val isLoading : Boolean = false,
    val isUpdating : Boolean = false,
    val error: String? = null
)
