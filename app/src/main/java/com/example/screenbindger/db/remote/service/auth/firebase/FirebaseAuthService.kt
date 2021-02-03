package com.example.screenbindger.db.remote.service.auth.firebase


import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.LoginState
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.view.fragment.login.LoginStateObservable
import com.example.screenbindger.view.fragment.register.RegisterStateObservable
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
        loginStateObservable: LoginStateObservable
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginStateObservable.setValue(LoginState.Success())
                } else {
                    loginStateObservable.setValue(LoginState.Error(Exception("Failed to login !")))
                }
            }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        registerStateObservable: RegisterStateObservable
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerStateObservable.setValue(RegisterState.Success())
                } else {
                    registerStateObservable.setValue(RegisterState.Error(Exception("Failed to register !")))
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