package com.example.taskflow.presentation.home

import androidx.lifecycle.ViewModel
import com.example.taskflow.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val user = authRepository.currentUser
        val displayName = user?.displayName?.ifBlank { null } ?: user?.email?.substringBefore("@") ?: "Alex"

        val initialTasks = listOf(
            TaskItem(
                id = "1",
                title = "Finalize Q4 Strategy",
                category = "Work",
                time = "09:00 AM",
                isCompleted = true
            ),
            TaskItem(
                id = "2",
                title = "Review design tokens",
                category = "Product",
                time = "",
                isCompleted = false,
                isHighPriority = true
            ),
            TaskItem(
                id = "3",
                title = "Client meeting: TaskFlow update",
                category = "Meeting",
                time = "02:30 PM",
                isCompleted = false
            )
        )

        val initialProjects = listOf(
            ProjectItem(id = "p1", name = "Mobile App", taskCount = 88, iconType = "folder"),
            ProjectItem(id = "p2", name = "AI Engine", taskCount = 12, iconType = "sparkles")
        )

        _uiState.update {
            it.copy(
                userName = displayName,
                todayTasks = initialTasks,
                recentProjects = initialProjects
            )
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        _uiState.update { state ->
            val updatedTasks = state.todayTasks.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
            val completedCount = updatedTasks.count { it.isCompleted }
            val total = updatedTasks.size
            val percentage = if (total > 0) (completedCount * 100) / total else 0
            state.copy(
                todayTasks = updatedTasks,
                completedTaskCount = completedCount,
                totalTaskCount = total,
                progressPercentage = percentage
            )
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.update { it.copy(isLoggedOut = true) }
    }
}
