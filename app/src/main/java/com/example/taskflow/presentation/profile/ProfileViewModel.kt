package com.example.taskflow.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.remote.firestore.FirestoreProfileDataSource
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

    private val firestoreProfileDataSource: FirestoreProfileDataSource
) : ViewModel() {


    private val _photoUrl = MutableStateFlow<String?>(null)
    val photoUrl = _photoUrl.asStateFlow()
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
        loadPhotoUrl()
    }

    private fun loadPhotoUrl() {

        viewModelScope.launch {

            try {

                _photoUrl.value = firestoreProfileDataSource.getPhotoUrl()
            } catch (e: Exception){

                _uiState.update {
                    it.copy(
                        error = e.message
                    )
                }
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {

        viewModelScope.launch {

            try {

                _uiState.update {
                    it.copy(
                        isUpdating = true,
                        error = null
                    )
                }

                // Get current image URL
                val oldPhotoUrl =
                    _photoUrl.value

                // Upload new image
                val newPhotoUrl =
                    firestoreProfileDataSource
                        .uploadProfileImage(uri)

                // Save new URL in Firestore
                firestoreProfileDataSource
                    .savePhotoUrl(newPhotoUrl)

                // Update UI immediately
                _photoUrl.value = newPhotoUrl

                // Delete previous image
                if (
                    !oldPhotoUrl.isNullOrBlank() &&
                    oldPhotoUrl != newPhotoUrl
                ) {
                    firestoreProfileDataSource
                        .deleteOldProfileImage(oldPhotoUrl)
                }

                _uiState.update {
                    it.copy(
                        isUpdating = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isUpdating = false,
                        error = e.message
                    )
                }
            }
        }
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