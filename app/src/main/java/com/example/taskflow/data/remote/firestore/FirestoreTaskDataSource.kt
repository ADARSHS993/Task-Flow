package com.example.taskflow.data.remote.firestore

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreTaskDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){

    private fun getUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User is not logged in")
    }

    private fun taskData(task: Task): Map<String, Any?> {

        return mapOf(
            "id" to task.id,
            "title" to task.title,
            "description" to task.description,
            "priority" to task.priority.name,
            "dueDate" to task.dueDate,
            "isCompleted" to task.isCompleted,
            "projectId" to task.projectId,
            "categoryId" to task.categoryId,
            "createdAt" to task.createdAt,
            "updatedAt" to task.updatedAt
        )
    }

    suspend fun addTask(task: Task) {

        val uid = getUserId()

        firestore
            .collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .set(taskData(task))
            .await()
    }

    suspend fun updateTask(task: Task) {

        val uid = getUserId()

        firestore
            .collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .set(taskData(task))
            .await()
    }

    suspend fun deleteTask(taskId: String) {

        val uid = getUserId()

        firestore
            .collection("users")
            .document(uid)
            .collection("tasks")
            .document(taskId)
            .delete()
            .await()
    }

    suspend fun getTasks(): List<Task>{

        val uid = getUserId()

        val snapshot = firestore
            .collection("users")
            .document(uid)
            .collection("tasks")
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->

            val id = document.getString("id") ?: document.id
            val title = document.getString("title") ?: return@mapNotNull null
            val description = document.getString("description") ?: ""

            val priorityString =
                document.getString("priority") ?: "LOW"

            val priority = try {
                Priority.valueOf(priorityString)
            } catch (e: Exception) {
                Priority.LOW
            }

            Task(
                id = id,
                title = title,
                description = description,
                priority = priority,
                dueDate = document.getLong("dueDate"),
                isCompleted =
                    document.getBoolean("isCompleted") ?: false,
                projectId =
                    document.getString("projectId"),
                categoryId =
                    document.getString("categoryId"),
                createdAt =
                    document.getLong("createdAt")
                        ?: System.currentTimeMillis(),
                updatedAt =
                    document.getLong("updatedAt")
                        ?: System.currentTimeMillis()
            )
        }
    }


}