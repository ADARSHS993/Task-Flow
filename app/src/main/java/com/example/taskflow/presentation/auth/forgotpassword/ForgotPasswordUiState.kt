package com.example.taskflow.presentation.auth.forgotpassword

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val generalError: String? = null,
    val isResetEmailSent: Boolean = false
)
