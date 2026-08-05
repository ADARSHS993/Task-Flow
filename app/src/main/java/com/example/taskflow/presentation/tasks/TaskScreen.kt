package com.example.taskflow.presentation.tasks

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onAddTaskClick: () -> Unit,
    onTaskClick: (String) -> Unit,
) {


}

