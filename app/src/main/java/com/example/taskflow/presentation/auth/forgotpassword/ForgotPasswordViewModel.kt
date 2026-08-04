package com.example.taskflow.presentation.auth.forgotpassword

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
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(generalError = null) }
    }

    fun dismissSuccessDialog() {
        _uiState.update { it.copy(isResetEmailSent = false) }
    }

    fun sendResetLink() {
        val email = _uiState.value.email.trim()

        if (email.isEmpty()) {
            _uiState.update { it.copy(emailError = "Email address is required") }
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(emailError = "Enter a valid email address") }
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isResetEmailSent = true) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = throwable.localizedMessage ?: "Failed to send reset email."
                    )
                }
            }
        }
    }
}
