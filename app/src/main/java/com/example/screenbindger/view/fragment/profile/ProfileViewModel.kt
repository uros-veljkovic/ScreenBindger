package com.example.screenbindger.view.fragment.profile

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel
@Inject constructor(
    val remoteDb: ScreenBindgerRemoteDatabase,
    var userStateObservable: UserStateObservable,
    val fragmentStateObservable: FragmentStateObservable
) : ViewModel() {

    fun fetchUser() {
        CoroutineScope(Dispatchers.IO).launch {
            remoteDb.fetchUser(userStateObservable)
        }
    }

    fun enableEdit() {
        when (fragmentStateObservable.state.value) {
            FragmentState.Editable -> {
                fragmentStateObservable.setState(FragmentState.NotEditable)
                CoroutineScope(Dispatchers.IO).launch {
                    remoteDb.updateUser(userStateObservable)
                }
            }
            FragmentState.NotEditable -> {
                fragmentStateObservable.setState(FragmentState.Editable)
            }
            else -> {
            }
        }
    }

    fun changePassword(newPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteDb.changePassword(newPassword, userStateObservable)
        }
    }
}