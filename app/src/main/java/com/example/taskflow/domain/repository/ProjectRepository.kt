package com.example.taskflow.domain.repository

import com.example.taskflow.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {

    fun getAllProjects(): Flow<List<Project>>

    fun getRecentProjects(): Flow<List<Project>>

    suspend fun addProject(project : Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject(project: Project)

    suspend fun getProjectById(projectId: String): Project?
}