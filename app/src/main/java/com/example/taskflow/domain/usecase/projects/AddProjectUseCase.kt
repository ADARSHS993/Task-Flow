package com.example.taskflow.domain.usecase.projects

import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.repository.ProjectRepository
import javax.inject.Inject

class AddProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {

    suspend operator fun invoke(project : Project){
        return repository.addProject(project)
    }
}