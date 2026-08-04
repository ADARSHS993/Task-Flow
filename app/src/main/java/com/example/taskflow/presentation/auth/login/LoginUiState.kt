package com.example.taskflow.presentation.auth.login

data class LoginUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val generalError: String? = null,
    val isLoginSuccess: Boolean = false
)
