package com.example.taskflow.data.remote.firestore

import com.example.taskflow.domain.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreCategoryDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private fun getUserId(): String{
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User is not logged in")
    }

    suspend fun addCategory(category: Category){

        val uid = getUserId()

        val data  = mapOf(
            "id" to category.id,
            "name" to category.name,
            "createdAt" to category.createAt,
            "updatedAt" to category.updateAt
        )

        firestore.collection("users")
            .document(uid)
            .collection("categories")
            .document(category.id)
            .set(data)
            .await()
    }

    suspend fun updateCategory(category: Category) {

        val uid = getUserId()

        val data = mapOf(
            "id" to category.id,
            "name" to category.name,
            "createdAt" to category.createAt,
            "updatedAt" to category.updateAt
        )

        firestore
            .collection("users")
            .document(uid)
            .collection("categories")
            .document(category.id)
            .set(data)
            .await()
    }

    suspend fun deleteCategory(categoryId: String) {

        val uid = getUserId()

        firestore
            .collection("users")
            .document(uid)
            .collection("categories")
            .document(categoryId)
            .delete()
            .await()
    }

    suspend fun getCategories(): List<Category> {

        val uid = getUserId()

        val snapshot = firestore
            .collection("users")
            .document(uid)
            .collection("categories")
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->

            val id =
                document.getString("id") ?: document.id

            val name =
                document.getString("name")
                    ?: return@mapNotNull null

            Category(
                id = id,
                name = name,
                createAt =
                    document.getLong("createdAt")
                        ?: System.currentTimeMillis(),
                updateAt =
                    document.getLong("updatedAt")
                        ?: System.currentTimeMillis(),
            )
        }
    }
}