package com.example.screenbindger.di.dagger.onboarding

import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
object OnboardingModule {

    @OnboardingScope
    @Provides
    fun provideUser(): UserObservable = UserObservable()
}