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
    private val remoteDb: ScreenBindgerRemoteDatabase,
    var userStateObservable: UserStateObservable,
    val fragmentStateObservable: FragmentStateObservable
) : ViewModel() {

    init {
        loadInfo()
    }

    private fun loadInfo() = CoroutineScope(IO).launch {
        remoteDb.fetchProfilePicture(userStateObservable)
        remoteDb.fetchUser(userStateObservable)
    }

    fun changeUiMode() {
        when (fragmentStateObservable.state.value) {
            FragmentState.Editable -> {
                fragmentStateObservable.setState(FragmentState.NotEditable)
                updateUser()
            }
            FragmentState.NotEditable -> {
                fragmentStateObservable.setState(FragmentState.Editable)
            }
            else -> {
            }
        }
    }

    private fun updateUser() {
        CoroutineScope(IO).launch {
            remoteDb.updateUser(userStateObservable)
        }
    }

    fun changePassword(newPassword: String) {
        CoroutineScope(IO).launch {
            remoteDb.changePassword(newPassword, userStateObservable)
        }
    }

    fun uploadImage(uri: Uri) {
        userStateObservable.setProfilePictureUri(uri)
        CoroutineScope(IO).launch {
            remoteDb.uploadImage(uri, userStateObservable)
        }
    }

}