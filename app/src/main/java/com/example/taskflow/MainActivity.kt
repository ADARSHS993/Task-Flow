package com.example.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.navigation.AppNavGraph
import com.example.taskflow.presentation.Theme.ThemeViewModel
import com.example.taskflow.ui.theme.TaskFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {


            val isDarkMode by themeViewModel.isDarkMode
                .collectAsStateWithLifecycle()

            TaskFlowTheme(
                darkTheme = isDarkMode
            ) {
                AppNavGraph(
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}