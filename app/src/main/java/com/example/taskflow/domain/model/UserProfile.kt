package com.example.taskflow.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String?= null,
)
