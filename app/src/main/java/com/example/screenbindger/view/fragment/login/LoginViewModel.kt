package com.example.screenbindger.view.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.entity.user.UserEntity
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.local.repo.ScreenBindgerLocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginViewModel
@ViewModelInject constructor(
    val user: UserObservable,
    val db: ScreenBindgerLocalDatabase
) : ViewModel() {

    var userFound: MutableLiveData<Boolean?> = MutableLiveData(null)
    var userAuthorized: MutableLiveData<Boolean?> = MutableLiveData(null)

    fun login() {
        runBlocking {
            launch { findUser() }
        }
    }

    private suspend fun findUser() {
        val user = db.find(user.toEntity())
        if(user == null){
            userFound.postValue(false)
        }else{
            authorizeUser()
        }
    }

    private suspend fun authorizeUser() {
        val user = db.authorize(user.toEntity())
        if(user == null){
            userAuthorized.postValue(false)
        }else{
            userAuthorized.postValue(true)
            db.login(user)
        }
    }

}