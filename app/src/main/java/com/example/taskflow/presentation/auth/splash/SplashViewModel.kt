package com.example.taskflow.presentation.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkAuthenticationState()
    }

    private fun checkAuthenticationState() {
        viewModelScope.launch {
            delay(1500)
            if (authRepository.isUserLoggedIn()) {
                _uiState.value = SplashUiState.NavigateToHome
            } else {
                _uiState.value = SplashUiState.NavigateToLogin
            }
        }
    }
}
