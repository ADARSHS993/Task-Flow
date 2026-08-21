package com.example.taskflow.domain.usecase.projects

import com.example.taskflow.domain.repository.ProjectRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String) =
        repository.getProjectById(projectId)
}