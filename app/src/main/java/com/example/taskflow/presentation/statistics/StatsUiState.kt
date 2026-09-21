package com.example.taskflow.presentation.statistics

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.model.Task
import java.time.LocalDate

data class StatsUiState(
    val tasks: List<Task> = emptyList(),
    val projects: List<Project> = emptyList(),
    val categories: List<Category> = emptyList(),

    val selectedWeekStart: LocalDate = LocalDate.now()
        .minusDays((LocalDate.now().dayOfWeek.value - 1).toLong()),

    val isLoading: Boolean = false,
    val error: String? = null
)