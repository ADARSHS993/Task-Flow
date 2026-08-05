package com.example.taskflow.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.task.AddTaskUseCase
import com.example.taskflow.domain.usecase.task.DeleteTaskUseCase
import com.example.taskflow.domain.usecase.task.GetAllTasksUSeCase
import com.example.taskflow.domain.usecase.task.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUSeCase,

    private val addTaskUseCase: AddTaskUseCase,

    private val updateTaskUseCase: UpdateTaskUseCase,

    private val deleteTaskUseCase: DeleteTaskUseCase,
): ViewModel(){
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllTasksUseCase
    }

    private fun getAllTasks() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            getAllTasksUseCase().collect { tasks ->

                _uiState.update {
                    it.copy(
                        tasks = tasks,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addTask(task : Task){
        viewModelScope.launch {
            addTaskUseCase(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }

    fun toggleTask(task: Task) {
        updateTask(
            task.copy(
                isCompleted = !task.isCompleted
            )
        )
    }

}