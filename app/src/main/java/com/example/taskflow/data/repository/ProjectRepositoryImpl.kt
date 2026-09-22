package com.example.taskflow.data.repository

import com.example.taskflow.data.local.dao.ProjectDao
import com.example.taskflow.data.mapper.toDomain
import com.example.taskflow.data.mapper.toEntity
import com.example.taskflow.data.remote.firestore.FirestoreProjectDataSource
import com.example.taskflow.domain.model.Project
import com.example.taskflow.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val firestoreProjectDataSource: FirestoreProjectDataSource
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
        projectDao.insertProject(
            project.toEntity()
        )

        firestoreProjectDataSource.addProject(project)
    }

    override suspend fun updateProject(project: Project) {
        projectDao.updateProject(
            project.toEntity()
        )

        firestoreProjectDataSource.updateProject(project)
    }

    override suspend fun deleteProject(project: Project) {
        projectDao.deleteProject(
            project.toEntity()
        )

        firestoreProjectDataSource.deleteProject(project.id)
    }

    override suspend fun getProjectById(projectId: String): Project? {
        return projectDao.getProjectById(projectId)?.toDomain()
    }

    override suspend fun syncProjectFromFirestore() {
        val projects =
            firestoreProjectDataSource.getProjects()

        projects.forEach { project ->

            projectDao.insertProject(
                project.toEntity()
            )
        }
    }
}