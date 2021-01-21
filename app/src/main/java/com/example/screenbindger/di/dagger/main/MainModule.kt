package com.example.screenbindger.di.dagger.main

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.screenbindger.R
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.Module
import dagger.Provides
import kotlinx.android.synthetic.main.activity_main.*

@Module
class MainModule {

/*    @MainScope
    @Provides
    fun provideNavHostFragment(activity: MainActivity): NavHostFragment {
        val fragmentManager = activity.supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        return fragment as NavHostFragment
    }

    @MainScope
    @Provides
    fun provideNavController(navHostFragment: NavHostFragment): NavController {
        return navHostFragment.findNavController()
    }*/

}