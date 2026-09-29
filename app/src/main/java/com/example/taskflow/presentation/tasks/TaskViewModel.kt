package com.example.taskflow.presentation.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.usecase.projects.GetAllProjectsUseCase
import com.example.taskflow.domain.usecase.task.AddCategoryUseCase
import com.example.taskflow.domain.usecase.task.AddTaskUseCase
import com.example.taskflow.domain.usecase.task.DeleteCategoryUsecase
import com.example.taskflow.domain.usecase.task.DeleteTaskUseCase
import com.example.taskflow.domain.usecase.task.GetAllTasksUSeCase
import com.example.taskflow.domain.usecase.task.GetCategoryUseCase
import com.example.taskflow.domain.usecase.task.GetTasksByProjectUseCase
import com.example.taskflow.domain.usecase.task.UpdateTaskUseCase
import com.example.taskflow.presentation.notification.TaskReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(

    private val getAllTasksUseCase: GetAllTasksUSeCase,
    private val getTasksByProjectUseCase : GetTasksByProjectUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,

    private val getCategoryUseCase : GetCategoryUseCase,
    private val addCategoryUseCase : AddCategoryUseCase,
    private val deleteCategoryUseCase : DeleteCategoryUsecase,

    private val getAllProjectsUseCase : GetAllProjectsUseCase,

    @ApplicationContext private val context: Context
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllTasks()
        getAllCategories()
        getAllProjects()
    }

    private fun getAllProjects(){
        viewModelScope.launch {

            getAllProjectsUseCase().collect { projects ->

                _uiState.update {
                    it.copy(
                        projects = projects
                    )
                }
            }
        }
    }

    fun getTasksByProject(projectId: String){
        viewModelScope.launch {
            getTasksByProjectUseCase(projectId).collect{ tasks ->
                _uiState.update {
                    it.copy(
                        tasks = tasks,
                        isLoading = false
                    )
                }
            }
        }
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

    fun deleteCategory(category: Category){
        viewModelScope.launch {

            deleteCategoryUseCase(category)
        }
    }

    fun getAllTasks() {

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
        priority: Priority,
        categoryId: String?,
        dueDate: Long?,
        projectId: String?
    ) {

        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            priority = priority,
            dueDate = dueDate,
            projectId = projectId,
            isCompleted = false,
            categoryId = categoryId,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            addTaskUseCase(task)

            TaskReminderScheduler.scheduleTaskReminders(
                context = context,
                taskId = task.id,
                taskTitle = task.title,
                dueDate = task.dueDate
            )
        }
    }

    fun updateTask(task: Task) {

        viewModelScope.launch {
            updateTaskUseCase(task)

            val updateTask = task.copy(
                updatedAt = System.currentTimeMillis()
            )

            updateTaskUseCase(updateTask)

            if(updateTask.isCompleted){
                TaskReminderScheduler.cancelTaskReminders(
                    context = context,
                    taskId = updateTask.id
                )
            } else{

                TaskReminderScheduler.scheduleTaskReminders(
                    context = context,
                    taskId = updateTask.id,
                    taskTitle = updateTask.title,
                    dueDate = updateTask.dueDate
                )
            }
        }

    }

    fun editTask(
        oldTask: Task,
        title: String,
        description: String,
        priority: Priority,
        categoryId: String?,
        dueDate: Long?,
        projectId: String?
    ) {

        val updatedTask = oldTask.copy(
            title = title,
            description = description,
            priority = priority,
            categoryId = categoryId,
            dueDate = dueDate,
            projectId = projectId,
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {

            // First cancel old reminders
            TaskReminderScheduler.cancelTaskReminders(
                context = context,
                taskId = updatedTask.id
            )

            // Save updated task
            updateTaskUseCase(updatedTask)

            // Schedule new reminders only if still pending
            if (!updatedTask.isCompleted) {
                TaskReminderScheduler.scheduleTaskReminders(
                    context = context,
                    taskId = updatedTask.id,
                    taskTitle = updatedTask.title,
                    dueDate = updatedTask.dueDate
                )
            }
        }
    }

    fun deleteTask(task: Task) {

        viewModelScope.launch {
            deleteTaskUseCase(task)

            TaskReminderScheduler.cancelTaskReminders(
                context = context,
                taskId = task.id
            )
        }

    }

    fun toggleTask(task: Task) {

        val updatedTask = task.copy(
            isCompleted = !task.isCompleted,
            updatedAt = System.currentTimeMillis()
        )


        viewModelScope.launch {

            updateTaskUseCase(updatedTask)

            if (updatedTask.isCompleted) {

                // Task completed -> cancel reminders
                TaskReminderScheduler.cancelTaskReminders(
                    context = context,
                    taskId = updatedTask.id
                )

            } else {

                // Task reopened -> schedule again
                TaskReminderScheduler.scheduleTaskReminders(
                    context = context,
                    taskId = updatedTask.id,
                    taskTitle = updatedTask.title,
                    dueDate = updatedTask.dueDate
                )
            }
        }

    }

    fun onSearchQueryChange(query: String) {

        _uiState.update {
            it.copy(searchQuery = query)
        }

    }

}