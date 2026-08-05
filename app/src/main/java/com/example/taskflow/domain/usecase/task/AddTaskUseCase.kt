package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository

class AddTaskUseCase( private val repository: TaskRepository){

    suspend operator fun invoke(task: Task){
        repository.insertTask(task)
    }
}