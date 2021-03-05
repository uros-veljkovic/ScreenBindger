package com.example.screenbindger.view.activity.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.activity_main.*
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
                    R.id.trendingFragment,
                    R.id.favoriteMoviesFragment
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

    fun modifyToolbarForFragment() {

        val transparentBackground =
            ContextCompat.getDrawable(this, R.drawable.background_toolbar)
        binding.toolbar.background = (transparentBackground)

        val constraintLayout = this.container

        ConstraintSet().also {

            it.clone(constraintLayout)

            it.connect(
                nav_host_fragment_activity_main.id,
                ConstraintSet.TOP,
                binding.toolbar.id,
                ConstraintSet.TOP,
                0
            )
            it.applyTo(constraintLayout)
        }
    }

    fun modifyToolbarForActivity() {

        val transparentBackground =
            ContextCompat.getColor(this, R.color.defaultBackground)
        binding.toolbar.setBackgroundColor(transparentBackground)

        val constraintLayout = binding.container

        ConstraintSet().also {

            it.clone(constraintLayout)

            it.connect(
                nav_host_fragment_activity_main.id,
                ConstraintSet.TOP,
                binding.toolbar.id,
                ConstraintSet.BOTTOM,
                0
            )
            it.applyTo(constraintLayout)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}