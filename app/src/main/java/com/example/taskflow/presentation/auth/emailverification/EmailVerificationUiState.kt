package com.example.taskflow.presentation.auth.emailverification

data class EmailVerificationUiState(
    val email: String = "",
    val isVerified: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
