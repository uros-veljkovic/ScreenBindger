package com.example.screenbindger.di.dagger.main

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.view.fragment.profile.FragmentStateObservable
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideUser(): UserObservable = UserObservable()

    @MainScope
    @Provides
    fun provideChangeableFragmentStateObservable(): FragmentStateObservable {
        return FragmentStateObservable()
    }

    @MainScope
    @Provides
    fun provideUserState(): ObjectState<UserObservable> {
        return ObjectState.InitialState<UserObservable>()
    }

    @MainScope
    @Provides
    fun provideUserStateObservable(
        userObservable: UserObservable,
        userState: ObjectState<UserObservable>
    ): UserStateObservable {
        return UserStateObservable(MutableLiveData(userState), userObservable)
    }


}