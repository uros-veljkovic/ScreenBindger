package com.example.screenbindger.view.fragment.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentLoginBinding
import com.example.screenbindger.model.state.LoginState
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snack
import com.example.screenbindger.util.extensions.startActivityWithDelay
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        observeFieldValidation()
        configWebView()
        initOnClickListeners()

        return view
    }

    fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun observeFieldValidation() {
        binding.apply {
            btnLogin.enableWhenUsing(FieldValidator()) {
                etUserEmail.isNotEmpty()
                etUserPassword.isNotEmpty()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun configWebView() {
        binding.wvCreateSession.apply {
            webViewClient = WebViewClient()

            settings.setSupportZoom(false)
            settings.javaScriptEnabled = true

            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.action == MotionEvent.ACTION_UP
                    && canGoBack()
                ) {
                    goBack()
                    return@setOnKeyListener true
                } else {
                    elevation = 0f
                    return@setOnKeyListener false
                }
            }
        }
    }

    private fun initOnClickListeners() {
        binding.apply {
            tvHere.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun showWebView() {
        binding.wvCreateSession.apply {
            elevation = resources.getDimension(R.dimen.normal_100)
            loadUrl("https://www.google.com/")
        }
    }


    override fun onResume() {
        super.onResume()

        observeUserAuthorized()
    }

    private fun observeUserAuthorized() {
        viewModel.loginStateObservable.value.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is LoginState.Success -> {
                    binding.progressBar.hide()
                    showWebView()
                    gotoMainActivity()
                }
                is LoginState.Error -> {
                    binding.progressBar.hide()
                    val message = state.exception.message ?: "Unknown error"
                    showError(message)
                }
                is LoginState.Loading -> {
                    binding.progressBar.show()
                }
                is LoginState.Rest -> {

                }
                else -> {

                }
            }
        })
    }

    private fun showError(message: String) {
        requireView().snack(message, R.color.logout_red)
    }

    fun gotoMainActivity() {
        animateButton()
        binding.progressBar.hide()
        startActivityWithDelay(MainActivity(), 1000)
    }

    private fun animateButton() {
        binding.btnLogin.also {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fadeout)
            it.startAnimation(animation)
            it.animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    it.visibility = View.INVISIBLE
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}