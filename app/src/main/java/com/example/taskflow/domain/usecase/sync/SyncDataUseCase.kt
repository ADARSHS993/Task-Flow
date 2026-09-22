package com.example.taskflow.domain.usecase.sync

import com.example.taskflow.domain.repository.ProjectRepository
import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class SyncDataUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository
) {

    suspend operator fun invoke(){

        projectRepository.syncProjectFromFirestore()

        taskRepository.syncTasksFromFirestore()

        taskRepository.syncCategoriesFromFirestore()
    }
}