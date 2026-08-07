package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.repository.TaskRepository

class DeleteCategoryUsecase(private val repository: TaskRepository) {

    suspend operator fun invoke(category: Category){
        repository.deleteCategory(category)
    }
}