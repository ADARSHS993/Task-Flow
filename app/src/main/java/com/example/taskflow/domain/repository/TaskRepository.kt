package com.example.taskflow.domain.repository

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    suspend fun getTaskById(id: String): Task?

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    fun  getCategories() : Flow<List<Category>>

    suspend fun  insertCategory(category : Category)

    suspend fun  deleteCategory(category : Category)
}