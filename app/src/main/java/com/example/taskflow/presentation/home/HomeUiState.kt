package com.example.taskflow.presentation.home

data class TaskItem(
    val id: String,
    val title: String,
    val category: String,
    val time: String,
    val isCompleted: Boolean = false,
    val isHighPriority: Boolean = false
)

data class ProjectItem(
    val id: String,
    val name: String,
    val taskCount: Int,
    val iconType: String
)

data class HomeUiState(
    val userName: String = "Alex",
    val dateString: String = "Sunday, August 2",
    val completedTaskCount: Int = 12,
    val totalTaskCount: Int = 16,
    val progressPercentage: Int = 75,
    val todayTasks: List<TaskItem> = emptyList(),
    val recentProjects: List<ProjectItem> = emptyList(),
    val isLoggedOut: Boolean = false
)
