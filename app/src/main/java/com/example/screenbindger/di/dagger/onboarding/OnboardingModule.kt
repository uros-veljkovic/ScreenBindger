package com.example.screenbindger.di.dagger.onboarding

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.remote.service.auth.AuthStateObservable
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.state.State
import dagger.Module
import dagger.Provides

@Module
object OnboardingModule {

    @OnboardingScope
    @Provides
    fun provideUser(): UserObservable = UserObservable()

    @OnboardingScope
    @Provides
    fun provideAuthResult(): AuthStateObservable {
        return AuthStateObservable(MutableLiveData(State.Unrequested))
    }

    @OnboardingScope
    @Provides
    fun provideUserStateObservable(user: UserObservable): UserStateObservable {
        return UserStateObservable(
            MutableLiveData(ObjectState.InitialState(user)),
            user
        )
    }


}