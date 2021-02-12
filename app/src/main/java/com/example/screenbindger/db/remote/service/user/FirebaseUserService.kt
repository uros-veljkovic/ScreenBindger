package com.example.screenbindger.db.remote.service.user

import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.model.state.ObjectState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import javax.inject.Inject

const val USER_COLLECTION = "users"

class FirebaseUserService @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserService {

    private val TAG = "FirebaseUserService"

    override suspend fun create(
        userStateObservable: UserStateObservable
    ) {
        auth.currentUser?.uid?.let { id ->
            database.collection(USER_COLLECTION)
                .document(id)
                .set(userStateObservable.user)
                .addOnSuccessListener {
                    userStateObservable.setState(ObjectState.Created())
                }.addOnFailureListener {
                    userStateObservable.setState(
                        ObjectState.Error(Exception("Failed to create account !"))
                    )
                }
        }
    }

    override suspend fun read(
        userStateObservable: UserStateObservable
    ) {
        auth.currentUser?.let { currUser ->
            database
                .collection(USER_COLLECTION)
                .document(currUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val user: UserEntity? = document.toObject(UserEntity::class.java)

                    if (user != null) {
                        userStateObservable.user = user
                        userStateObservable.setState(ObjectState.Read(userStateObservable.user))
                    } else {
                        userStateObservable.setState(ObjectState.Error(Exception("Failed to find user data")))
                    }
                }
                .addOnFailureListener { _ ->
                    userStateObservable.setState(ObjectState.Error(Exception("Failed to read user data !")))
                }
        } ?: userStateObservable.setState(ObjectState.Error(Exception("No logged in user !")))

    }

    override suspend fun delete(
        userStateObservable: UserStateObservable
    ) {
        auth.currentUser?.let { currUser ->
            database.collection(USER_COLLECTION).document(currUser.uid)
                .delete()
                .addOnSuccessListener {
                    userStateObservable.setState(ObjectState.Deleted())
                }
                .addOnFailureListener {
                    userStateObservable.setState(ObjectState.Error(Exception("Error deleting profile")))
                }
        }
    }

    override suspend fun update(
        userStateObservable: UserStateObservable
    ) {
        auth.currentUser?.let { currUser ->
            database.collection(USER_COLLECTION)
                .document(currUser.uid)
                .get()
                .addOnSuccessListener { doc ->
                    val map = mapOf(
                        "fullName" to userStateObservable.user.fullName,
                        "dateOfBirth" to userStateObservable.user.dateOfBirth
                    )
                    doc.reference.update(map).addOnSuccessListener {
                        userStateObservable.setState(ObjectState.Updated())
                    }.addOnFailureListener {
                        userStateObservable.setState(ObjectState.Error(Exception("Error updating profile.")))
                    }
                }.addOnFailureListener {
                    userStateObservable.setState(ObjectState.Error(Exception("Failed finding user document.")))
                }
        }
    }

}