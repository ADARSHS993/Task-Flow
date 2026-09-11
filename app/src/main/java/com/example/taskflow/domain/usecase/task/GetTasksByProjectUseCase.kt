package com.example.taskflow.domain.usecase.task

import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksByProjectUseCase @Inject constructor(
    private val repository : TaskRepository
) {
    operator fun invoke(projectId: String): Flow<List<Task>>{
        return repository.getTaskByProject(projectId)
    }
}