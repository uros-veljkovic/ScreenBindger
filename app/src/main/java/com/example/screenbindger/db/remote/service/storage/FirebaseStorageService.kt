package com.example.screenbindger.db.remote.service.storage

import android.net.Uri
import android.util.Log
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.Exception


class FirebaseStorageService : StorageService {

    private val TAG = "FirebaseStorageService"
    private val storage: StorageReference = FirebaseStorage.getInstance().reference

    override suspend fun downloadImage(userStateObservable: UserStateObservable) {
        val imagePath = "profile_images/${userStateObservable.user.id}"
        storage
            .child(imagePath)
            .downloadUrl
            .addOnSuccessListener {
                userStateObservable.user.imageUri = it.toString()
                userStateObservable.setState(ObjectState.Read())
            }
            .addOnFailureListener {
                userStateObservable.setState(ObjectState.Error(Exception()))
            }

    }

    override suspend fun uploadImage(uri: Uri, userStateObservable: UserStateObservable) {
        val ref = storage.child("profile_images/${userStateObservable.user.id}")

        val uploadTask = ref.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    userStateObservable.user.imageUri = downloadUri.toString()
                    userStateObservable.setState(ObjectState.Read(userStateObservable.user))
                } else {
                    userStateObservable.setState(ObjectState.Error(Exception("Unable to upload a picture")))
                }
            }
    }

    override suspend fun deleteImage() {
        TODO("Not yet implemented")
    }

}