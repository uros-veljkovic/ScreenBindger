package com.example.screenbindger.di.hilt.module.viewmodel

import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class RegisterViewModelModule {

    @Provides
    fun provideUserObservable(): UserObservable{
        return UserObservable()
    }

}