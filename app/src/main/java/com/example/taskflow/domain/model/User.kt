package com.example.taskflow.domain.model

data class User(
    val uid: String,
    val email: String,
    val fullName: String = "",
    val isEmailVerified: Boolean = false
)
