package com.example.screenbindger.db.remote.service.auth


import android.util.Log
import com.example.screenbindger.util.state.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception
import javax.inject.Inject


class FirebaseAuthService
@Inject constructor(
    private val auth: FirebaseAuth,
    private var currentUser: FirebaseUser?
) : AuthService {

    override suspend fun signIn(
        email: String,
        password: String,
        stateObservable: AuthStateObservable
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setCurrentUserAndPostResult(stateObservable)
                } else {
                    stateObservable.setValue(State.Error(Exception("Failed to login !")))
                }
            }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        stateObservable: AuthStateObservable
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setCurrentUserAndPostResult(stateObservable)
                } else {
                    stateObservable.setValue(State.Error(Exception("Failed to register !")))
                }
            }
    }

    private fun setCurrentUserAndPostResult(stateObservable: AuthStateObservable) {
        currentUser = auth.currentUser
        Log.d("TAG", "setCurrentUserAndPostResult: ${auth.currentUser}")
        stateObservable.setValue(State.Success(currentUser!!))
    }

    override suspend fun signOut() {
        auth.signOut()
    }

}