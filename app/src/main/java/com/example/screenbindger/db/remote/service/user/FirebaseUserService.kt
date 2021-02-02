package com.example.screenbindger.db.remote.service.user

import android.util.Log
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.ObjectState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import java.lang.Exception
import javax.inject.Inject

const val USER_COLLECTION = "users"

class FirebaseUserService @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference
) : UserService {

    private val TAG = "FirebaseUserService"

    override suspend fun create(
        userStateObservable: UserStateObservable
    ) {
        database.collection(USER_COLLECTION)
            .add(userStateObservable.user)
            .addOnSuccessListener { docRef ->
                docRef.get().addOnSuccessListener { docSnapshot ->
                    val user = docSnapshot.toObject(UserEntity::class.java)!!
                    userStateObservable.user = user
                }.addOnFailureListener {
                    userStateObservable.setState(ObjectState.Error(Exception("Failed to load user data !")))
                }
                userStateObservable.setState(ObjectState.Created())
            }.addOnFailureListener {
                userStateObservable.setState(
                    ObjectState.Error(Exception("Failed to create account !"))
                )
            }
    }

    override suspend fun read(
        userStateObservable: UserStateObservable
    ) {
        auth.currentUser?.let { currUser ->
            database
                .collection(USER_COLLECTION)
                .whereEqualTo("email", currUser.email)
                .get()
                .addOnSuccessListener { documents ->
                    var user: UserEntity? = null
                    for (doc in documents) {
                        user = doc.toObject(UserEntity::class.java)
                    }
                    if (user != null) {
                        userStateObservable.user = user
                        getProfilePicture(userStateObservable)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "read: ${exception.message}")
                    userStateObservable.setState(ObjectState.Error(Exception("Failed to read user data !")))
                }
        }
            ?: userStateObservable.setState(ObjectState.Error(Exception("No logged in user !")))

    }

    private fun getProfilePicture(userStateObservable: UserStateObservable) {
        val imagePath = "profile_images/${userStateObservable.user.id}"
        storage
            .child(imagePath)
            .downloadUrl
            .addOnSuccessListener {
                userStateObservable.setState(ObjectState.Read(userStateObservable.user))
            }
            .addOnFailureListener {
//                userStateObservable.setState(ObjectState.Read())
            }
    }

    override suspend fun delete(
        userStateObservable: UserStateObservable
    ) {
        database.collection(USER_COLLECTION).document(auth.currentUser!!.email!!)
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override suspend fun update(
        userStateObservable: UserStateObservable
    ) {
        database.collection(USER_COLLECTION)
            .whereEqualTo("email", auth.currentUser!!.email)
            .get()
            .addOnSuccessListener {
                it.documents.forEach { document: DocumentSnapshot ->
                    val map = mapOf(
                        "fullName" to userStateObservable.user.fullName,
                        "dateOfBirth" to userStateObservable.user.dateOfBirth
                    )
                    document.reference.update(map).addOnSuccessListener {
                        userStateObservable.setState(ObjectState.Updated())
                    }.addOnFailureListener {
                        userStateObservable.setState(ObjectState.Error(Exception("Error updating profile")))
                    }
                    return@forEach
                }
            }.addOnFailureListener {
                userStateObservable.setState(ObjectState.Error(Exception("Error updating profile")))
            }
    }

}