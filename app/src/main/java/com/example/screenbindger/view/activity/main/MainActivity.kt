package com.example.screenbindger.view.activity.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.screenbindger.R
import com.example.screenbindger.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var appBarConfiguration: AppBarConfiguration

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

    fun setupToolbar() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(binding.toolbar)
        binding.toolbar.refreshDrawableState()
        supportActionBar?.show()
    }

    private fun setupBottomNavBar() {
        val config = AppBarConfiguration(setOf(
            R.id.profileFragment,
            R.id.genresFragment,
            R.id.upcomingFragment,
            R.id.trendingFragment
        ))
        setupActionBarWithNavController(navController, config)


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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profileFragment -> {
                navController.navigate(R.id.action_global_profileFragment)
            }
        }
        return true
    }
}