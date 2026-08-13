package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor( private val repository: TaskRepository){

    suspend operator fun invoke(task: Task){
        repository.insertTask(task)
    }
}