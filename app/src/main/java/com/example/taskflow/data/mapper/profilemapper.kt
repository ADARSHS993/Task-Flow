package com.example.taskflow.data.mapper

import com.example.taskflow.domain.model.UserProfile
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUserProfile(): UserProfile{

    return UserProfile(
        uid = uid,
        name = displayName ?: "",
        email = email ?: "",
        photoUrl = photoUrl?.toString()
    )
}