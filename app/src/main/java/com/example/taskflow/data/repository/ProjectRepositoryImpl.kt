package com.example.taskflow.data.repository

import com.example.taskflow.data.local.dao.ProjectDao
import com.example.taskflow.data.mapper.toDomain
import com.example.taskflow.data.mapper.toEntity
import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao
): ProjectRepository {
    override fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAllProjects().map { projects ->
            projects.map {
                it.toDomain()
            }
        }
    }

    override fun getRecentProjects(): Flow<List<Project>> {
        return projectDao.getRecentProjects().map { projects ->
            projects.map {
                it.toDomain()
            }
        }
    }

    override suspend fun addProject(project: Project) {
        return projectDao.insertProject(project.toEntity())
    }

    override suspend fun updateProject(project: Project) {
        return projectDao.updateProject(project.toEntity())
    }

    override suspend fun deleteProject(project: Project) {
        return projectDao.deleteProject(project.toEntity())
    }

    override suspend fun getProjectById(projectId: String): Project? {
        return projectDao.getProjectById(projectId)?.toDomain()
    }
}