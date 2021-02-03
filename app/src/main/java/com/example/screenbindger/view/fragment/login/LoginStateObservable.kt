package com.example.screenbindger.view.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.LoginState
import com.example.screenbindger.util.state.StateObservable
import org.w3c.dom.Entity
import javax.inject.Inject

class LoginStateObservable
@Inject constructor(
    
) : StateObservable<LoginState<UserEntity>>()