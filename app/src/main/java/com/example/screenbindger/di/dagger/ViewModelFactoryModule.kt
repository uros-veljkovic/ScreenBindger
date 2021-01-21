package com.example.screenbindger.di.dagger

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bind(
        factory: ViewModelProviderFactory
    ): ViewModelProvider.Factory
}