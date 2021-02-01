package com.example.screenbindger.view.activity.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.screenbindger.R
import com.example.screenbindger.databinding.ActivityMainBinding
import com.example.screenbindger.view.fragment.profile.ProfileViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        setupNavigation()
        fetchGenres()
    }

    private fun setupNavigation() {
        setupToolbar()
        setupBottomNavBar()
    }

    private fun setupToolbar() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(binding.toolbar)

        with(
            AppBarConfiguration(
                setOf(
                    R.id.profileFragment,
                    R.id.genresFragment,
                    R.id.upcomingFragment,
                    R.id.trendingFragment
                )
            )
        ) {
            setupActionBarWithNavController(navController, this)
        }

    }

    private fun setupBottomNavBar() {
        NavigationUI.setupWithNavController(
            binding.bottomNav,
            navController
        )
    }

    private fun fetchGenres() {
        viewModel.fetchGenres()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}