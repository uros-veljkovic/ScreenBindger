package com.example.screenbindger.view.activity.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.screenbindger.R
import com.example.screenbindger.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGlobalNavigation()
        fetchGenres()
    }

    private fun setupGlobalNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment?
        NavigationUI.setupWithNavController(
            binding.bottomNav,
            navHostFragment!!.navController
        )
    }

    private fun fetchGenres(){
        viewModel.fetchGenres()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profileFragment -> {
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_global_profileFragment)
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}