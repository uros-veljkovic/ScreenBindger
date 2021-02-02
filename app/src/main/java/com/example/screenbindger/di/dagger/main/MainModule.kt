package com.example.screenbindger.di.dagger.main

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.view.fragment.profile.FragmentStateObservable
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideUser(): UserEntity = UserEntity()

    @MainScope
    @Provides
    fun provideChangeableFragmentStateObservable(): FragmentStateObservable {
        return FragmentStateObservable()
    }

    @MainScope
    @Provides
    fun provideUserState(userEntity: UserEntity): ObjectState<UserEntity> {
        return ObjectState.InitialState(userEntity)
    }

    @MainScope
    @Provides
    fun provideUserStateObservable(
        userState: ObjectState<UserEntity>,
        userEntity: UserEntity
    ): UserStateObservable {
        return UserStateObservable(MutableLiveData(userState), userEntity)
    }


}