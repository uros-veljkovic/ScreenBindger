package com.example.screenbindger.di.dagger.onboarding

import com.example.screenbindger.view.fragment.login.LoginFragment
import com.example.screenbindger.view.fragment.register.RegisterFragment
import com.example.screenbindger.view.fragment.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OnboardingFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment
}