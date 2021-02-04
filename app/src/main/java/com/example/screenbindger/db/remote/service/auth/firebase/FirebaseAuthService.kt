package com.example.screenbindger.db.remote.service.auth.firebase


import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import javax.inject.Inject


class FirebaseAuthService
@Inject constructor(
    private val auth: FirebaseAuth
) : AuthService {

    override suspend fun signIn(
        email: String,
        password: String,
        authStateObservable: AuthorizationStateObservable
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authStateObservable.setValue(AuthState.FirebaseAuthSuccess())
                } else {
                    authStateObservable.setValue(
                        AuthState.Error.FirebaseAuthFailed(
                            Exception("Failed to login !")
                        )
                    )
                }
            }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        authStateObservable: AuthorizationStateObservable
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authStateObservable.setValue(AuthState.FirebaseAuthSuccess())
                } else {
                    authStateObservable.setValue(
                        AuthState.Error.FirebaseAuthFailed(
                            Exception("Failed to register !")
                        )
                    )
                }
            }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    ) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                userStateObservable.setState(ObjectState.Updated())
            }?.addOnFailureListener {
                userStateObservable.setState(ObjectState.Error(Exception("Failed to update user !")))
            }
    }

}