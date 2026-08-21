package com.example.taskflow.presentation.home

import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.model.Task

data class HomeUiState(
    val tasks: List<Task> = emptyList(),

    val todayTasks : List<Task> = emptyList(),

    val recentProjects : List<Project> = emptyList(),

    val completedTasks : Int = 0,

    val totalTasks: Int = 0,

    val progressPercentage: Int = 0,

    val isLoading: Boolean = false,

    val error: String? = null
    )
