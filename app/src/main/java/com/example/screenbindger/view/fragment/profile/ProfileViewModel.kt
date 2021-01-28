package com.example.screenbindger.view.fragment.profile

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.local.repo.ScreenBindgerLocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel
@Inject constructor(
    val db: ScreenBindgerLocalDatabase
): ViewModel(){

    var user: UserObservable? = null
    var isEditMode = ObservableField(false)
    var updated = MutableLiveData(false)

/*    fun fetchData(){
        CoroutineScope(IO).launch {
            user = db.findLoggedInUser().toObservable()
        }
    }*/

    fun changeFieldEnablability(){
        isEditMode.set(isEditMode.get()!!.xor(true))
        isEditMode.notifyChange()

        if(!isEditMode.get()!!){
//            persistUser()
        }
    }

    /*fun setUserImage(imageUri: Uri?) {
        user?.imageUri = imageUri.toString()
    }

    fun persistUser(){
        CoroutineScope(IO).launch {
            if (user != null) {
                db.update(user!!.toEntity())
                updated.postValue(true)
            }
        }
    }

    fun logout() {
        CoroutineScope(IO).launch {
            if (user != null) {
                db.logout(user!!.toEntity())
            }
        }
    }*/
}