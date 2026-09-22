package com.example.taskflow.data.repository

import com.example.taskflow.data.local.dao.CategoryDao
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.mapper.toDomain
import com.example.taskflow.data.mapper.toEntity
import com.example.taskflow.data.remote.firestore.FirestoreCategoryDataSource
import com.example.taskflow.data.remote.firestore.FirestoreTaskDataSource
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao,
    private val categoryDao : CategoryDao,
    private val firestoreCategoryDataSource: FirestoreCategoryDataSource,
    private val firestoreTaskDataSource: FirestoreTaskDataSource
): TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {

            return dao.getAllTasks().map{ list ->
                list.map {
                    it.toDomain()
                }
        }
        }

    override fun getTaskByProject(projectId: String): Flow<List<Task>> {
        return dao.getTasksByProject(projectId)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }


    override suspend fun getTaskById(id: String): Task? {
        return dao.getTaskById(id)?.toDomain()
    }

    override suspend fun insertTask(task: Task) {
        dao.insertTask(
            task.toEntity()
        )

        firestoreTaskDataSource.addTask(task)
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(
            task.toEntity()
        )

        firestoreTaskDataSource.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
         dao.deleteTask(
             task.toEntity()
         )

        firestoreTaskDataSource.deleteTask(task.id)
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories().map { list ->
                list.map {
                    it.toDomain()
                }
            }
    }

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(
            category.toEntity()
        )

        firestoreCategoryDataSource.addCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
         categoryDao.deleteCategory(
             category.toEntity()
         )

        firestoreCategoryDataSource.deleteCategory(
            category.id
        )
    }

    override suspend fun syncTasksFromFirestore() {
        val tasks =
            firestoreTaskDataSource.getTasks()

        tasks.forEach { task ->

            dao.insertTask(
                task.toEntity()
            )
        }
    }

    override suspend fun syncCategoriesFromFirestore() {
        val categories =
            firestoreCategoryDataSource.getCategories()

        categories.forEach { category ->

            categoryDao.insertCategory(
                category.toEntity()
            )
        }
    }


}