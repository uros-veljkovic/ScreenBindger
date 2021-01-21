package com.example.screenbindger.di.dagger

import com.example.screenbindger.di.dagger.main.MainFragmentBuildersModule
import com.example.screenbindger.di.dagger.main.MainModule
import com.example.screenbindger.di.dagger.main.MainScope
import com.example.screenbindger.di.dagger.main.MainViewModelsModule
import com.example.screenbindger.di.dagger.onboarding.OnboardingFragmentBuildersModule
import com.example.screenbindger.di.dagger.onboarding.OnboardingModule
import com.example.screenbindger.di.dagger.onboarding.OnboardingScope
import com.example.screenbindger.di.dagger.onboarding.OnboardingViewModelsModule
import com.example.screenbindger.view.activity.main.MainActivity
import com.example.screenbindger.view.activity.onboarding.OnboardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @OnboardingScope
    @ContributesAndroidInjector(
        modules = [
            OnboardingModule::class,
            OnboardingFragmentBuildersModule::class,
            OnboardingViewModelsModule::class
        ]
    )
    abstract fun contributeOnboardingActivity(): OnboardingActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainModule::class,
            MainFragmentBuildersModule::class,
            MainViewModelsModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity

}