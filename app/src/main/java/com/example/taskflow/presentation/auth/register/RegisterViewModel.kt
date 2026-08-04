package com.example.taskflow.presentation.auth.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, fullNameError = null, generalError = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, generalError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun clearError() {
        _uiState.update { it.copy(generalError = null) }
    }

    fun register() {
        val fullName = _uiState.value.fullName.trim()
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        var hasError = false

        if (fullName.isEmpty()) {
            _uiState.update { it.copy(fullNameError = "Full Name is required") }
            hasError = true
        }

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

        if (confirmPassword.isEmpty()) {
            _uiState.update { it.copy(confirmPasswordError = "Please confirm password") }
            hasError = true
        } else if (password != confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            hasError = true
        }

        if (hasError) return

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            val result = authRepository.register(email, password, fullName)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isRegisterSuccess = true) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = throwable.localizedMessage ?: "Registration failed. Please try again."
                    )
                }
            }
        }
    }

    fun resetRegisterSuccessState() {
        _uiState.update { it.copy(isRegisterSuccess = false) }
    }
}
