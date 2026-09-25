package com.example.taskflow.data.remote.firestore

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreProfileDataSource @Inject constructor(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
){

    private fun getUid(): String{
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User is not logged in")

    }

    suspend fun uploadProfileImage(uri: Uri): String{

        val uid = getUid()

        val filename = "profile_${System.currentTimeMillis()}.jpg"

        val imageRef = storage
            .reference
            .child("users/$uid/profile/$filename")

        imageRef.putFile(uri).await()

        return imageRef.downloadUrl.await().toString()
    }

    suspend fun savePhotoUrl(photoUrl: String){

        val uid = getUid()

        firestore
            .collection("users")
            .document(uid)
            .set(
                mapOf(
                    "photoUrl" to photoUrl
                ),
               SetOptions.merge()
            )
            .await()
    }

    suspend fun getPhotoUrl(): String? {

        val uid = getUid()

        val snapshot = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()

        return snapshot.getString("photoUrl")
    }

    suspend fun deleteOldProfileImage(
        oldPhotoUrl: String?
    ) {
        if (oldPhotoUrl.isNullOrBlank()) return

        try {
            val oldReference =
                storage.getReferenceFromUrl(oldPhotoUrl)

            oldReference.delete().await()
        } catch (e: Exception) {
            // Ignore if old image doesn't exist
        }
    }
}