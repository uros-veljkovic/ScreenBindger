package com.example.screenbindger.di.dagger.onboarding

import androidx.lifecycle.ViewModel
import com.example.screenbindger.di.dagger.ViewModelKey
import com.example.screenbindger.view.fragment.login.LoginFragmentViewModel
import com.example.screenbindger.view.fragment.register.RegisterFragmentViewModel
import com.example.screenbindger.view.fragment.splash.SplashFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OnboardingViewModelsModule {


    @Binds
    @IntoMap
    @ViewModelKey(RegisterFragmentViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashFragmentViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginFragmentViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginFragmentViewModel): ViewModel


}