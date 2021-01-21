package com.example.screenbindger.view.activity.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.screenbindger.R
import dagger.android.support.DaggerAppCompatActivity

class OnboardingActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }
}