package com.example.screenbindger.view.fragment.login

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.auth.AuthStateObservable
import com.example.screenbindger.util.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel
@Inject constructor(
    val user: UserObservable,
    val remoteDb: ScreenBindgerRemoteDatabase,
    val stateObservable: AuthStateObservable
) : ViewModel() {


    fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            stateObservable.setValue(State.Loading)
            remoteDb.login(user, stateObservable)
        }
    }
    /*var userFound: MutableLiveData<Boolean?> = MutableLiveData(null)
    var userAuthorized: MutableLiveData<Boolean?> = MutableLiveData(null)

    fun login() {
        runBlocking {
            launch { findUser() }
        }
    }

    private suspend fun findUser() {
        val user = db.find(user.toEntity())
        if (user == null) {
            userFound.postValue(false)
        } else {
            authorizeUser()
        }
    }

    private suspend fun authorizeUser() {
        val user = db.authorize(user.toEntity())
        if (user == null) {
            userAuthorized.postValue(false)
        } else {
            userAuthorized.postValue(true)
            db.login(user)
        }
    }*/

}