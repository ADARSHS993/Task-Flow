package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.repository.TaskRepository

class GetCategoryUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke() = repository.getCategories()
}