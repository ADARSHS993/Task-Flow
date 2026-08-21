package com.example.taskflow.domain.usecase.projects

import com.example.taskflow.domain.repository.ProjectRepository
import javax.inject.Inject

class GetRecentProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke() = repository.getRecentProjects()
}