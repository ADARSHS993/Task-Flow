package com.example.taskflow.navigation

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object ForgotPassword : AppDestination("forgot_password")
    data object Home : AppDestination("home")
    data object Task : AppDestination("task")
    data object Calendar : AppDestination("calendar")
    data object Stats : AppDestination("stats")
    data object Profile : AppDestination("profile")
}