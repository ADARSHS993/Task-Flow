package com.example.taskflow.presentation.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                generalError = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                generalError = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun clearError() {
        _uiState.update { it.copy(generalError = null) }
    }

    fun login() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        var hasError = false

        if (email.isEmpty()) {
            _uiState.update { it.copy(emailError = "Email address is required") }
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(emailError = "Enter a valid email address") }
            hasError = true
        }

        if (password.isEmpty()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasError = true
        } else if (password.length < 6) {
            _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
            hasError = true
        }

        if (hasError) return

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess { user ->
                _uiState.update { it.copy(isLoading = false) }
                if (user != null) {
                    _uiState.update { it.copy(isLoginSuccess = true) }
                } else {
                    _uiState.update { it.copy(generalError = "User profile not found") }
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = throwable.localizedMessage ?: "Authentication failed. Please check credentials."
                    )
                }
            }
        }
    }

    fun resetLoginSuccessState() {
        _uiState.update { it.copy(isLoginSuccess = false) }
    }
}
