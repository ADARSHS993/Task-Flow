package com.example.taskflow.presentation.tasks


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addEditTaskScreen(
    task: Task? = null,

    onBack: () -> Unit,

    viewModel: TaskViewModel = hiltViewModel()
) {

}