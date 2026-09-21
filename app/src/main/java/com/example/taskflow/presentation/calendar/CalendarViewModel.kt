package com.example.taskflow.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskflow.domain.usecase.projects.GetAllProjectsUseCase
import com.example.taskflow.domain.usecase.task.GetAllTasksUSeCase
import com.example.taskflow.domain.usecase.task.GetCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUSeCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCalendarData()
    }

    private fun loadCalendarData() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            launch {
                getAllTasksUseCase().collect { tasks ->

                    _uiState.update {
                        it.copy(
                            tasks = tasks
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
                            categories = categories,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun selectDate(date: LocalDate) {

        _uiState.update {
            it.copy(
                selectedDate = date
            )
        }
    }

    fun previousMonth() {

        _uiState.update { state ->

            val newMonth =
                state.currentMonth.minusMonths(1)

            state.copy(
                currentMonth = newMonth,
                selectedDate = newMonth.withDayOfMonth(1)
            )
        }
    }

    fun nextMonth() {

        _uiState.update { state ->

            val newMonth =
                state.currentMonth.plusMonths(1)

            state.copy(
                currentMonth = newMonth,
                selectedDate = newMonth.withDayOfMonth(1)
            )
        }
    }
}