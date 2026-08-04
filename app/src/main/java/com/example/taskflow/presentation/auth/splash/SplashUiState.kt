package com.example.taskflow.presentation.auth.splash

sealed interface SplashUiState {
    data object Loading : SplashUiState
    data object NavigateToHome : SplashUiState
    data object NavigateToLogin : SplashUiState
}
