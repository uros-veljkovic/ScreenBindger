package com.example.screenbindger.di.dagger.main

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.screenbindger.R
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.di.dagger.onboarding.OnboardingScope
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.Module
import dagger.Provides
import kotlinx.android.synthetic.main.activity_main.*

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideUser(): UserObservable = UserObservable()


}