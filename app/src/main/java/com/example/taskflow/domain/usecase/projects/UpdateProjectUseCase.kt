package com.example.taskflow.domain.usecase.projects

import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.repository.ProjectRepository
import javax.inject.Inject

class UpdateProjectUseCase @Inject constructor(
    val repository : ProjectRepository
) {

    suspend operator fun invoke(project : Project) {
        return repository.updateProject(project)
    }
}