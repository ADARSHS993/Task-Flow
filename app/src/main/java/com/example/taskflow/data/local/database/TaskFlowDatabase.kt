package com.example.taskflow.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TaskFlowDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun categoryDao(): CategoryDao
}