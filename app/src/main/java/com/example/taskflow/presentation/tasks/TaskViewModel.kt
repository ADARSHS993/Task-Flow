package com.example.taskflow.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.task.AddCategoryUseCase
import com.example.taskflow.domain.usecase.task.AddTaskUseCase
import com.example.taskflow.domain.usecase.task.DeleteCategoryUsecase
import com.example.taskflow.domain.usecase.task.DeleteTaskUseCase
import com.example.taskflow.domain.usecase.task.GetAllTasksUSeCase
import com.example.taskflow.domain.usecase.task.GetCategoryUseCase
import com.example.taskflow.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(

    private val getAllTasksUseCase: GetAllTasksUSeCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,

    private val getCategoryUseCase : GetCategoryUseCase,
    private val addCategoryUseCase : AddCategoryUseCase,
    private val deleteCategoryUseCase : DeleteCategoryUsecase


    ) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllTasks()
        getAllCategories()
    }

    private fun getAllCategories() {

        viewModelScope.launch {

            getCategoryUseCase().collect { categories ->

                _uiState.update {
                    it.copy(categories = categories)
                }

            }

        }

    }

    fun addCategory(category: Category) {

        viewModelScope.launch {

            addCategoryUseCase(category)

        }

    }

    fun deleteCCategory(category: Category){
        viewModelScope.launch {

            deleteCategoryUseCase(category)
        }
    }

    private fun getAllTasks() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            getAllTasksUseCase().collectLatest { tasks ->

                _uiState.update {
                    it.copy(
                        tasks = tasks,
                        isLoading = false,
                        error = null
                    )
                }

            }

        }

    }

    fun saveTask(
        title: String,
        description: String,
        priority: Priority
    ) {

        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            priority = priority,
            dueDate = null,
            isCompleted = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            addTaskUseCase(task)
        }

    }

    fun updateTask(task: Task) {

        viewModelScope.launch {
            updateTaskUseCase(task)
        }

    }

    fun editTask(
        oldTask: Task,
        title: String,
        description: String,
        priority: Priority
    ) {

        val updatedTask = oldTask.copy(
            title = title,
            description = description,
            priority = priority,
            updatedAt = System.currentTimeMillis()
        )

        updateTask(updatedTask)

    }

    fun deleteTask(task: Task) {

        viewModelScope.launch {
            deleteTaskUseCase(task)
        }

    }

    fun toggleTask(task: Task) {

        val updatedTask = task.copy(
            isCompleted = !task.isCompleted,
            updatedAt = System.currentTimeMillis()
        )

        updateTask(updatedTask)

    }

    fun onSearchQueryChange(query: String) {

        _uiState.update {
            it.copy(searchQuery = query)
        }

    }

}