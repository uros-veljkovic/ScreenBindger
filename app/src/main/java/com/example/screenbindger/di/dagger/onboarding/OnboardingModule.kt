package com.example.screenbindger.di.dagger.onboarding

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.view.fragment.login.AuthorizationEventObservable
import com.example.screenbindger.view.fragment.register.RegisterEventObservable
import dagger.Module
import dagger.Provides

@Module
object OnboardingModule {

    @OnboardingScope
    @Provides
    fun provideUser(): UserEntity = UserEntity().apply {
        email = "uros@gmail.com"
        password = "lozinka"
    }

    @OnboardingScope
    @Provides
    fun provideLoginStateObservable(session: Session): AuthorizationEventObservable {
        return AuthorizationEventObservable(session)
    }

    @OnboardingScope
    @Provides
    fun provideRegisterStateObservable(): RegisterEventObservable {
        return RegisterEventObservable()
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