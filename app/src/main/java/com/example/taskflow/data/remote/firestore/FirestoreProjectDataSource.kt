package com.example.taskflow.data.remote.firestore

import com.example.taskflow.domain.model.Project
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreProjectDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    private fun getUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User is not logged in")
    }

    suspend fun addProject(project: Project) {

        val uid = getUserId()

        val data = mapOf(
            "id" to project.id,
            "name" to project.name,
            "description" to project.description,
            "color" to project.color,
            "createdAt" to project.createAt,
            "updatedAt" to project.updateAt
        )

        firestore
            .collection("users")
            .document(uid)
            .collection("projects")
            .document(project.id)
            .set(data)
            .await()
    }

    suspend fun updateProject(project: Project) {

        val uid = getUserId()

        val data = mapOf(
            "id" to project.id,
            "name" to project.name,
            "description" to project.description,
            "color" to project.color,
            "createdAt" to project.createAt,
            "updatedAt" to project.updateAt
        )

        firestore
            .collection("users")
            .document(uid)
            .collection("projects")
            .document(project.id)
            .set(data)
            .await()
    }

    suspend fun deleteProject(projectId: String) {

        val uid = getUserId()

        firestore
            .collection("users")
            .document(uid)
            .collection("projects")
            .document(projectId)
            .delete()
            .await()
    }

    suspend fun getProjects(): List<Project> {

        val uid = getUserId()

        val snapshot = firestore
            .collection("users")
            .document(uid)
            .collection("projects")
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->

            val id =
                document.getString("id") ?: document.id

            val name =
                document.getString("name")
                    ?: return@mapNotNull null

            Project(
                id = id,
                name = name,
                description =
                    document.getString("description") ?: "",
                color =
                    document.getString("color") ?: "",
                createAt =
                    document.getLong("createdAt")
                        ?: System.currentTimeMillis(),
                updateAt =
                    document.getLong("updatedAt")
                        ?: System.currentTimeMillis()
            )
        }
    }
}