package com.example.taskflow.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskflow.data.local.dao.CategoryDao
import com.example.taskflow.data.local.dao.ProjectDao
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.entity.CategoryEntity
import com.example.taskflow.data.local.entity.ProjectEntity
import com.example.taskflow.data.local.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class,
        ProjectEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class TaskFlowDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun categoryDao(): CategoryDao

    abstract fun projectDao(): ProjectDao
}