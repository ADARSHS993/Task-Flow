package com.example.taskflow.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.usecase.projects.GetAllProjectsUseCase
import com.example.taskflow.domain.usecase.task.GetAllTasksUSeCase
import com.example.taskflow.domain.usecase.task.GetCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StateViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUSeCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
    ): ViewModel() {

    private val _uiState = MutableStateFlow(
        StatsUiState()
    )

    val uiState = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            launch {
                getAllTasksUseCase().collect { tasks ->

                    _uiState.update {
                        it.copy(
                            tasks = tasks,
                            isLoading = false
                        )
                    }
                }
            }

            launch {
                getAllProjectsUseCase().collect { projects ->

                    _uiState.update {
                        it.copy(
                            projects = projects
                        )
                    }
                }
            }

            launch {
                getCategoryUseCase().collect { categories ->

                    _uiState.update {
                        it.copy(
                            categories = categories
                        )
                    }
                }
            }
        }
    }

    fun previousWeek() {

        _uiState.update {
            it.copy(
                selectedWeekStart =
                    it.selectedWeekStart.minusWeeks(1)
            )
        }
    }

    fun nextWeek() {

        _uiState.update {
            it.copy(
                selectedWeekStart =
                    it.selectedWeekStart.plusWeeks(1)
            )
        }
    }
}