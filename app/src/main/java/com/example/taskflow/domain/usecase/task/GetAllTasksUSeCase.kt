package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.repository.TaskRepository

class GetAllTasksUSeCase(
    private val repository: TaskRepository
) {
    operator fun invoke() = repository.getAllTasks()
}