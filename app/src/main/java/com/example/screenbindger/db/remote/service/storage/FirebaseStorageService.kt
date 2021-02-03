package com.example.screenbindger.db.remote.service.storage

import android.net.Uri
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.Exception
import javax.inject.Inject


class FirebaseStorageService
@Inject constructor(
    val auth: FirebaseAuth
) : StorageService {

    private val TAG = "FirebaseStorageService"
    private val storage: StorageReference = FirebaseStorage.getInstance().reference

    override suspend fun downloadImage(userStateObservable: UserStateObservable) {
        auth.currentUser?.uid.let { id ->
            val imagePath = "profile_images/${id}"
            storage
                .child(imagePath)
                .downloadUrl
                .addOnSuccessListener { uri ->
                    userStateObservable.setProfilePictureUri(uri)
                    userStateObservable.setState(ObjectState.Read())
                }
                .addOnFailureListener {
                    userStateObservable.setState(ObjectState.Error(Exception("No profile image found.")))
                }
        }

    }

    override suspend fun uploadImage(uri: Uri, userStateObservable: UserStateObservable) {
        auth.currentUser?.uid.let { id ->
            val storageFolderReference = storage.child("profile_images/${id}")

            val uploadTask = storageFolderReference.putFile(uri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userStateObservable.setState(ObjectState.Read(userStateObservable.user))
                    } else {
                        userStateObservable.setState(ObjectState.Error(Exception("Failed to upload a picture")))
                    }
                }
        }
    }

    override suspend fun deleteImage() {
        TODO("Not yet implemented")
    }

}