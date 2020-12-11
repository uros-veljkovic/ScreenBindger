package com.example.screenbindger.view.fragment.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.screenbindger.R
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onResume() {
        super.onResume()

        observeIfUserLoggedIn()
    }

    /**
     * This method navigates to LoginFragment.class if there is no logged in user
     * in local database, or to MainFragment.class if there is.
     */
    private fun observeIfUserLoggedIn() {
        viewModel.isLoggedIn().observe(viewLifecycleOwner, Observer { isLoggedIn ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (isLoggedIn == null) {
                    gotoLoginFragment()
                } else {
                    gotoMainActivity()
                }
            }, 1000)
        })
    }

    private fun gotoLoginFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }

    private fun gotoMainActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }
}