package com.example.screenbindger.view.fragment.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.screenbindger.R
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

        viewModel.isLoggedIn().observe(viewLifecycleOwner, Observer { isLoggedIn ->
            if (isLoggedIn != null) {
                Toast.makeText(context, "Logged in !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Not logged in...", Toast.LENGTH_SHORT).show()
            }
        })
    }
}