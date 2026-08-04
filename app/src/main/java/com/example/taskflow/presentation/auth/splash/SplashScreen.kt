package com.example.taskflow.presentation.auth.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        when (uiState) {
            SplashUiState.NavigateToHome -> onNavigateToHome()
            SplashUiState.NavigateToLogin -> onNavigateToLogin()
            SplashUiState.Loading -> {}
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF7F7FD),
            Color(0xFFF0F1FA),
            Color(0xFFEBF0FA)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TaskFlow",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E232A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Productivity Redefined.",
                fontSize = 16.sp,
                color = Color(0xFF757989),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp),
                color = Color(0xFF4F46E5),
                trackColor = Color(0xFFDCDFEA)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SYNCHRONIZING",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8C90A0),
                letterSpacing = 2.sp
            )
        }

        Text(
            text = "© 2024 TaskFlow Ecosystem",
            fontSize = 12.sp,
            color = Color(0xFFA0A4B4),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}
