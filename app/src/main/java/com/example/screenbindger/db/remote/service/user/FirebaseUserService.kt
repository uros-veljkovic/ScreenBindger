package com.example.screenbindger.db.remote.service.user

import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.lang.Exception
import javax.inject.Inject

const val USER_COLLECTION = "users"

class FirebaseUserService @Inject constructor(
    private val database: FirebaseFirestore
) : UserService {

    //TODO: Postaviti smislen documentID
    override suspend fun create(
        user: UserObservable,
        userActionStateObservable: UserActionStateObservable
    ) {
        database.collection(USER_COLLECTION).document()
            .set(user)
            .addOnSuccessListener {
                userActionStateObservable.setValue(UserActionState.Created)
            }.addOnFailureListener {
                userActionStateObservable.setValue(
                    UserActionState.Failed(Exception("Failed to create account !"))
                )
            }
    }

    override suspend fun read(
        user: UserObservable,
        userActionStateObservable: UserActionStateObservable
    ) {

        database
            .collection(USER_COLLECTION)
            .document(user.email)
            .get()
            .addOnSuccessListener { document ->

            }
            .addOnFailureListener { exception ->

            }
    }

    //TODO: Postaviti smislen documentID
    override suspend fun delete(
        user: UserObservable,
        userActionStateObservable: UserActionStateObservable
    ) {
        database.collection(USER_COLLECTION).document(user.email)
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    //TODO: Postaviti smislen documentID
    override suspend fun update(
        user: UserObservable,
        userActionStateObservable: UserActionStateObservable
    ) {
        database.collection(USER_COLLECTION).document(user.email)
            .set(user)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}