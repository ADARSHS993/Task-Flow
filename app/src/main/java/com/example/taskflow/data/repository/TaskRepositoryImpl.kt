package com.example.taskflow.data.repository

import com.example.taskflow.data.local.dao.CategoryDao
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.mapper.toDomain
import com.example.taskflow.data.mapper.toEntity
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao,
    private val categoryDao : CategoryDao
): TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {

            return dao.getAllTasks().map{ list ->
                list.map {
                    it.toDomain()
                }
        }
        }


    override suspend fun getTaskById(id: String): Task? {
        return dao.getTaskById(id)?.toDomain()
    }

    override suspend fun insertTask(task: Task) {
        return dao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        return dao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        return dao.deleteTask(task.toEntity())
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories().map { list ->
                list.map {
                    it.toDomain()
                }
            }
    }

    override suspend fun insertCategory(category: Category) {
         return categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        return categoryDao.deleteCategory(category.toEntity())
    }


}