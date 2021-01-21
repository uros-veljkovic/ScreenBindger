package com.example.screenbindger.app

import com.example.screenbindger.di.dagger.DaggerAppComponent
import dagger.android.DaggerApplication

class ScreenBindger : DaggerApplication() {
    override fun applicationInjector() =
        DaggerAppComponent.builder()
            .application(this)
            .build()
}