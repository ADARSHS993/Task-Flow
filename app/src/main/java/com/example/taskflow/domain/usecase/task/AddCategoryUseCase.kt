package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository

class AddCategoryUseCase(private val repository: TaskRepository) {

    suspend operator fun invoke(category: Category){
        repository.insertCategory(category)
    }
}