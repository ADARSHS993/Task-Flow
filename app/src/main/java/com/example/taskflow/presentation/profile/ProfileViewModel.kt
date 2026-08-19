package com.example.taskflow.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.usecase.profile.ChangePasswordUseCase
import com.example.taskflow.domain.usecase.profile.GetProfileUseCase
import com.example.taskflow.domain.usecase.profile.LogoutUseCase
import com.example.taskflow.domain.usecase.profile.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val profile = getProfileUseCase()

        _uiState.update {
            it.copy(
                profile = profile
            )
        }
    }

    fun updateProfile(name: String) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isUpdating = true,
                    error = null
                )
            }

            val result = updateProfileUseCase(name)

            if (result.isSuccess) {
                loadProfile()

                _uiState.update {
                    it.copy(
                        isUpdating = false
                    )
                }
            } else {

                _uiState.update {
                    it.copy(
                        isUpdating = false,
                        error =
                            result.exceptionOrNull()
                                ?.message
                    )
                }
            }

        }
    }

    fun changePassword(password: String) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isUpdating = true,
                    error = null
                )
            }

            val result =
                changePasswordUseCase(password)

            _uiState.update {

                it.copy(
                    isUpdating = false,
                    error =
                        result.exceptionOrNull()
                            ?.message
                )

            }
        }
    }

    fun logout() {

        logoutUseCase()

    }
}