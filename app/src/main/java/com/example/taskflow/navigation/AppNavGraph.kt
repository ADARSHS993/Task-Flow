package com.example.taskflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.example.taskflow.presentation.auth.login.LoginScreen
import com.example.taskflow.presentation.auth.register.RegisterScreen
import com.example.taskflow.presentation.auth.splash.SplashScreen
import com.example.taskflow.presentation.home.HomeScreen
import com.example.taskflow.presentation.tasks.TaskScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestination.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Destination
        composable(AppDestination.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Destination
        composable(AppDestination.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppDestination.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AppDestination.ForgotPassword.route)
                }
            )
        }

        // Register Destination
        composable(AppDestination.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Forgot Password Destination
        composable(AppDestination.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Home Destination
        composable(AppDestination.Home.route) {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

    }
}
