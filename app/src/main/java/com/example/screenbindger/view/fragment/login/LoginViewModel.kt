package com.example.screenbindger.view.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.db.ScreenBindgerDatabase
import com.example.screenbindger.db.local.entity.user.UserEntity
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginViewModel
@ViewModelInject constructor(
    val user: UserObservable,
    val db: ScreenBindgerDatabase
) : ViewModel() {

    var loginTrigger: MutableLiveData<Boolean?> = MutableLiveData(null)
    private var foundUser: UserEntity? = null

    fun login() {
        runBlocking {
            val loginJob: Job = findUser()
            loginJob.join()
            loginIfFound()
        }
    }

    private fun findUser(): Job {
        return CoroutineScope(IO).launch {
            foundUser = db.find(user.toEntity())
        }
    }

    private suspend fun loginIfFound() {
        if (foundUser != null) {
            loginTrigger.postValue(true)
            db.login(foundUser!!)
        } else {
            loginTrigger.postValue(false)
        }
    }

}