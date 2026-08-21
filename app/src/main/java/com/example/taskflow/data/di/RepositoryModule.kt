package com.example.taskflow.data.di

import com.example.taskflow.data.repository.ProfileRepositoryImpl
import com.example.taskflow.data.repository.ProjectRepositoryImpl
import com.example.taskflow.data.repository.TaskRepositoryImpl
import com.example.taskflow.domain.repository.ProfileRepository
import com.example.taskflow.domain.repository.ProjectRepository
import com.example.taskflow.domain.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        repositoryImpl : TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl : ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindProjectRepository(
        repositoryImpl: ProjectRepositoryImpl
    ): ProjectRepository
}