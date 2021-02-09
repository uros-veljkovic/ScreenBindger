package com.example.screenbindger.view.fragment.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentLoginBinding
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.util.constants.REQUEST_CODE_TOKEN_AUTH
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snack
import com.example.screenbindger.util.extensions.startActivityWithDelay
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private fun initOnClickListeners() {
        binding.tvHere.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
        viewModel.also {
            it.authStateObservable.value.observe(viewLifecycleOwner, Observer { event ->
                event.getContentIfNotHandled()?.let { state ->
                    when (state) {
                        is AuthState.FirebaseAuthSuccess -> {
                            it.fetchToken()
                        }
                        is AuthState.TokenFetched -> {
                            val token = state.getToken()
                            viewModel.token = token!!
                            authorizeToken(token)
                        }
                        is AuthState.TokenAuthorized -> {
                            it.startSession()
                        }
                        is AuthState.SessionStarted -> {
                            it.fetchAccountDetails()
                        }
                        is AuthState.AccountDetailsFetched -> {
                            hideProgressBar()
                            it.setSession(state.session)
                            gotoMainActivity()
                        }
                        is AuthState.Error.SessionStartFailed -> {
                            showSessionFailDialog(state.e.message)
                        }
                        is AuthState.Error -> {
                            hideProgressBar()
                            val message = state.getMessage()
                            showError(message ?: "Unknown error")
                        }
                        is AuthState.Loading -> {
                            showProgressBar()
                        }
                        is AuthState.Rest -> {
                            hideProgressBar()
                        }
                        else -> {
                        }
                    }
                }
            })
        }
    }

    private fun showSessionFailDialog(message: String?) {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.Widget_ScreenBindger_MaterialAlertDialog
        )
            .setTitle(resources.getString(R.string.message_title_token_auth_failed))
            .setMessage(message)
            .setNeutralButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.setState(AuthState.FirebaseAuthSuccess())
            }
            .show()
    }

    private fun showError(message: String) {
        Log.d(TAG, "observeFragmentActions: $message")
        requireView().snack(message, R.color.logout_red)
    }

    private fun authorizeToken(token: String?) {

        val uri = Uri.parse("https://www.themoviedb.org/authenticate/$token")
        val i = Intent(Intent.ACTION_VIEW, uri)
        startActivityForResult(i, REQUEST_CODE_TOKEN_AUTH)

    }

    private fun gotoMainActivity() {
        viewModel.setState(AuthState.Rest)
        animateButton()
        hideProgressBar()
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

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_TOKEN_AUTH) {
            viewModel.setState(AuthState.TokenAuthorized(viewModel.token))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}