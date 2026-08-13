package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke() = repository.getCategories()
}