package com.example.screenbindger.di.dagger.onboarding

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import com.example.screenbindger.view.fragment.register.RegisterStateObservable
import dagger.Module
import dagger.Provides

@Module
object OnboardingModule {

    @OnboardingScope
    @Provides
    fun provideUser(): UserEntity = UserEntity()

    @OnboardingScope
    @Provides
    fun provideLoginStateObservable(): AuthorizationStateObservable {
        return AuthorizationStateObservable()
    }

    @OnboardingScope
    @Provides
    fun provideRegisterStateObservable(): RegisterStateObservable {
        return RegisterStateObservable()
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