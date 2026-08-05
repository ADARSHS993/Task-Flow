package com.example.taskflow.di

import android.content.Context
import androidx.room.Room
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.database.TaskFlowDatabase
import com.example.taskflow.data.repository.TaskRepositoryImpl
import com.example.taskflow.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context : Context
    ): TaskFlowDatabase{

        return Room.databaseBuilder(
            context,
            TaskFlowDatabase::class.java,
            "taskflow_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(
        database: TaskFlowDatabase
    ): TaskDao{
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository{
        return TaskRepositoryImpl(dao)
    }
}