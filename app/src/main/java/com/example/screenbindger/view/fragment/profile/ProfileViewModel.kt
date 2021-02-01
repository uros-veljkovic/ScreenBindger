package com.example.screenbindger.view.fragment.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ProfileViewModel
@Inject constructor(
    val remoteDb: ScreenBindgerRemoteDatabase,
    var userStateObservable: UserStateObservable,
    val fragmentStateObservable: FragmentStateObservable
) : ViewModel() {

    init {
        loadInfo()
    }


    private fun loadInfo() = CoroutineScope(IO).launch {
        remoteDb.fetchUser(userStateObservable)
    }

    fun changeUiMode() {
        when (fragmentStateObservable.state.value) {
            FragmentState.Editable -> {
                fragmentStateObservable.setState(FragmentState.NotEditable)
                CoroutineScope(IO).launch {
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
        CoroutineScope(IO).launch {
            remoteDb.changePassword(newPassword, userStateObservable)
        }
    }

    fun uploadImage(uri: Uri) {
        CoroutineScope(IO).launch {
            remoteDb.uploadImage(uri, userStateObservable)
        }
    }

}