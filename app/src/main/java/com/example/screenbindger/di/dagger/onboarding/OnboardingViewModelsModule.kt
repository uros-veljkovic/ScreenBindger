package com.example.screenbindger.di.dagger.onboarding

import androidx.lifecycle.ViewModel
import com.example.screenbindger.di.dagger.ViewModelKey
import com.example.screenbindger.view.fragment.login.LoginViewModel
import com.example.screenbindger.view.fragment.register.RegisterViewModel
import com.example.screenbindger.view.fragment.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OnboardingViewModelsModule {


    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel


}