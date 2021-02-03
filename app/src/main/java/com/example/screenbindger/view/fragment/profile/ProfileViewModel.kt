package com.example.screenbindger.view.fragment.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel
@Inject constructor(
    private val remoteDataSource: ScreenBindgerRemoteDataSource,
    var userStateObservable: UserStateObservable,
    val fragmentStateObservable: FragmentStateObservable
) : ViewModel() {

    init {
        loadInfo()
    }

    private fun loadInfo() = CoroutineScope(IO).launch {
        remoteDataSource.fetchProfilePicture(userStateObservable)
        remoteDataSource.fetchUser(userStateObservable)
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
            remoteDataSource.updateUser(userStateObservable)
        }
    }

    fun changePassword(newPassword: String) {
        CoroutineScope(IO).launch {
            remoteDataSource.changePassword(newPassword, userStateObservable)
        }
    }

    fun uploadImage(uri: Uri) {
        userStateObservable.setProfilePictureUri(uri)
        CoroutineScope(IO).launch {
            remoteDataSource.uploadImage(uri, userStateObservable)
        }
    }

}