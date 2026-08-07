package com.example.taskflow.presentation.tasks

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Task

data class TaskUiState(

    val tasks: List<Task> = emptyList(),

    val categories: List<Category> = emptyList(),

    val searchQuery: String = "",

    val isLoading: Boolean = false,

    val error: String? = null
)
