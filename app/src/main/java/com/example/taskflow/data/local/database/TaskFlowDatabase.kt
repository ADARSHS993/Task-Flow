package com.example.taskflow.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TaskFlowDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}