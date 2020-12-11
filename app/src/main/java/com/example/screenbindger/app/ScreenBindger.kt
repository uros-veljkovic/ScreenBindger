package com.example.screenbindger.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScreenBindger : Application() {
    companion object {
        var application: ScreenBindger? = null

        fun context(): Context {
            return application!!.applicationContext
        }

    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}