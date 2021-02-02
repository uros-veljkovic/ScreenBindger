package com.example.screenbindger.di.dagger.onboarding

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
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
    fun provideUser(): UserEntity = UserEntity()

    @OnboardingScope
    @Provides
    fun provideAuthResult(): AuthStateObservable {
        return AuthStateObservable(MutableLiveData(State.Unrequested))
    }

    @OnboardingScope
    @Provides
    fun provideProfilePictureUriObservable(): MutableLiveData<Uri?> {
        return MutableLiveData(null)
    }

    @OnboardingScope
    @Provides
    fun provideUserStateObservable(
        user: UserEntity,
        profilePictureUriObservable: MutableLiveData<Uri?>
    ): UserStateObservable {

        return UserStateObservable(
            MutableLiveData(ObjectState.InitialState(user)),
            profilePictureUriObservable,
            user
        )
    }


}