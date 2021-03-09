package com.example.screenbindger.view.fragment.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private fun loadInfo() = viewModelScope.launch(IO) {
        remoteDataSource.fetchProfilePicture(userStateObservable)
        remoteDataSource.fetchUser(userStateObservable)
    }

    fun changeUiMode() {
        when (fragmentStateObservable.state.value) {
            ProfileViewState.Editable -> {
                fragmentStateObservable.setState(ProfileViewState.NotEditable)
                updateUser()
            }
            ProfileViewState.NotEditable -> {
                fragmentStateObservable.setState(ProfileViewState.Editable)
            }
            else -> {
            }
        }
    }

    private fun updateUser() {
        viewModelScope.launch(IO) {
            remoteDataSource.updateUser(userStateObservable)
        }
    }

    fun changePassword(newPassword: String) {
        viewModelScope.launch(IO) {
            remoteDataSource.changePassword(newPassword, userStateObservable)
        }
    }

    fun uploadImage(uri: Uri) {
        userStateObservable.setProfilePictureUri(uri)
        viewModelScope.launch(IO) {
            remoteDataSource.uploadImage(uri, userStateObservable)
        }
    }

}