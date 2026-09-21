package com.example.taskflow.presentation.calendar

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.model.Task
import java.time.LocalDate

data class CalendarUiState(

    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: LocalDate = LocalDate.now().withDayOfMonth(1),

    val tasks: List<Task> = emptyList(),
    val projects: List<Project> = emptyList(),
    val categories: List<Category> = emptyList(),

    val isLoading : Boolean = false,
    val error: String? = null
){
    val selectedTasks: List<Task>
        get() = tasks.filter { task ->

            task.dueDate?.let { timestamp ->

                java.time.Instant
                    .ofEpochMilli(timestamp)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate() == selectedDate
            } ?: false
        }
}
