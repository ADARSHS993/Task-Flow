package com.example.taskflow.presentation.auth.emailverification

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
class EmailVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailVerificationUiState())
    val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    init {
        loadUserEmail()
    }

    private fun loadUserEmail() {
        val user = authRepository.currentUser
        _uiState.update {
            it.copy(
                email = user?.email ?: "",
                isVerified = user?.isEmailVerified == true
            )
        }
    }

    fun resendVerificationEmail() {
        _uiState.update { it.copy(isLoading = true, message = null, error = null) }
        viewModelScope.launch {
            val result = authRepository.sendEmailVerification()
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = "Verification email sent! Check your inbox."
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.localizedMessage ?: "Failed to resend verification email."
                    )
                }
            }
        }
    }

    fun checkVerificationStatus() {
        _uiState.update { it.copy(isLoading = true, message = null, error = null) }
        viewModelScope.launch {
            val result = authRepository.reloadUser()
            result.onSuccess { isVerified ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isVerified = isVerified,
                        message = if (isVerified) "Email verified successfully!" else "Email is not verified yet. Please check your inbox."
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.localizedMessage ?: "Failed to refresh verification status."
                    )
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun clearFeedback() {
        _uiState.update { it.copy(message = null, error = null) }
    }
}
