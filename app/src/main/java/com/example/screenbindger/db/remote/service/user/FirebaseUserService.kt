package com.example.screenbindger.db.remote.service.user

import android.util.Log
import com.example.screenbindger.db.local.entity.user.UserEntity
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.model.state.ObjectState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import javax.inject.Inject

const val USER_COLLECTION = "users"

class FirebaseUserService @Inject constructor(
    private val database: FirebaseFirestore,
    private var currentUser: FirebaseUser?
) : UserService {

    private val TAG = "FirebaseUserService"

    override suspend fun create(
        userStateObservable: UserStateObservable
    ) {
        database.collection(USER_COLLECTION).document()
            .set(userStateObservable.user)
            .addOnSuccessListener {
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

        database
            .collection(USER_COLLECTION)
            .document(currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                val userEntity = document.toObject(UserEntity::class.java)!!
                val user = userEntity.toObservable()
                userStateObservable.setObject(user)
                userStateObservable.setState(ObjectState.Read(user))
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "read: ${exception.message}")
                userStateObservable.setState(ObjectState.Error(Exception("Failed to read user data !")))
            }
    }

    override suspend fun delete(
        userStateObservable: UserStateObservable
    ) {
        database.collection(USER_COLLECTION).document(currentUser!!.email!!)
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override suspend fun update(
        userStateObservable: UserStateObservable
    ) {
        database.collection(USER_COLLECTION).document(currentUser!!.email!!)
            .set(userStateObservable.user)
            .addOnSuccessListener {
                userStateObservable.setState(ObjectState.Updated())
            }.addOnFailureListener {

            }
    }
}