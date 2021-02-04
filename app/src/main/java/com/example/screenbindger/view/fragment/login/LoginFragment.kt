package com.example.screenbindger.view.fragment.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentLoginBinding
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snack
import com.example.screenbindger.util.extensions.startActivityWithDelay
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class LoginFragment : DaggerFragment() {

    private val TAG = "LoginFragment"

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
            webChromeClient = object : WebChromeClient() {
                // popup webview!
                override fun onCreateWindow(
                    view: WebView,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message
                ): Boolean {
                    val newWebView: WebView = WebView(requireActivity()).apply {
                        settings.javaScriptEnabled = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.setSupportMultipleWindows(true)
                        layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            ) //making sure the popup opens full screen
                    }
                    view.addView(newWebView)
                    resultMsg.obj = newWebView
                    resultMsg.sendToTarget()
                    newWebView.webChromeClient = object : WebChromeClient() {
                        override fun onCloseWindow(window: WebView) {
                            super.onCloseWindow(window)

                            binding.wvCreateSession.removeView(newWebView)
                        }
                    }
                    return true
                }

                override fun onCloseWindow(window: WebView) {
                    super.onCloseWindow(window)
                    Log.d(TAG, "onCloseWindow: WEB_VIEW CLOSED")
                }

            }

            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.action == MotionEvent.ACTION_UP
                    && canGoBack()
                ) {
                    goBack()
                    return@setOnKeyListener true
                } else {
                    requireView().snack("Went home !")
                    elevation = 0f
                    return@setOnKeyListener false
                }
            }
            loadUrl("https://www.themoviedb.org/authenticate/${viewModel.requestToken}")
            elevation = 16f
        }

    }

    private fun initOnClickListeners() {
        binding.apply {
            tvHere.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            ivScreenBindger.setOnClickListener {
                configWebView()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        observeFragmentActions()
    }

    /*
        Todo: Make a fragment called AuthorizeTokenFragment with a WebView and a button to confirm
            that the user has authorized the token.
            When user clicks the button, he should be prompted to:
                1. If user is registering -> to Email app to verify his email
                2. If user is logging in -> back to ScreenBindger
     */
    private fun observeFragmentActions() {
        viewModel.apply {
            authStateObservable.value.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is AuthState.FirebaseAuthSuccess -> {
                        getToken()
                    }
                    is AuthState.TokenGathered -> {
                        this@LoginFragment.authorizeToken()
                    }
                    is AuthState.TokenAuthorized -> {
                        println("observe: Authorized token")
                        createSession()
                    }
                    is AuthState.SessionStarted -> {
                        gotoMainActivity()
                    }
                    is AuthState.Error -> {
                        binding.progressBar.hide()
                        val message = state.exception.message ?: "Unknown error"
                        showError(message)
                    }
                    is AuthState.Loading -> {
                        showProgressBar()
                    }
                    is AuthState.Rest -> {
                    }
                }
            })
        }
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun authorizeToken() {

    }

    private fun showError(message: String) {
        Log.d(TAG, "observeFragmentActions: $message")
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