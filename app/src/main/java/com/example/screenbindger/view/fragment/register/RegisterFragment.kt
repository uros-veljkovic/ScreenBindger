package com.example.screenbindger.view.fragment.register

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentRegisterBinding
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.model.state.ObjectState
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

class RegisterFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: RegisterViewModel

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        observeDatePicker()
        observeFieldValidation()
        initOnClickListeners()
        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun observeDatePicker() {
        binding.etUserDateOfBirth.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.etUserDateOfBirth.showDatePicker(parentFragmentManager)
        }
    }

    private fun observeFieldValidation() {
        binding.btnRegister.enableWhenUsing(FieldValidator()) {
            binding.apply {
                etUserFullName.isNotEmpty()
                etUserEmail.isNotEmpty()
                etUserPassword.isNotEmpty()
            }
        }
    }

    private fun initOnClickListeners() {
        binding.tvHere.setOnClickListener {
            gotoLoginFragment()
        }
    }

    private fun gotoMainActivity() {
        hideProgressBar()
        startActivityWithDelay(MainActivity(), 1000)

    }

    private fun gotoLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onResume() {
        super.onResume()

        observeRegistration()
    }

    private fun observeRegistration() {
        viewModel.also {
            it.authStateObservable.value.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is AuthState.FirebaseAuthSuccess -> {
                        it.requestToken()
                    }
                    is AuthState.TokenGathered -> {
                        state.event.getContentIfNotHandled()?.let { response ->
                            val token = response.requestToken!!
                            viewModel.token = token
                            authorizeToken(token)
                        }
                    }
                    is AuthState.TokenAuthorized -> {
                        it.createSession()
                    }
                    is AuthState.SessionStarted -> {
                        it.getAccountDetails()
                    }
                    is AuthState.AccountDetailsGathered -> {
                        hideProgressBar()
                        state.session.getContentIfNotHandled()?.let { session ->
                            it.setSession(session)
                            gotoMainActivity()
                        }
                    }
                    is AuthState.Error.SessionStartFailed -> {
                        val message = it.getErrorMessage()
                        showSessionFailDialog(message)
                    }
                    is AuthState.Error -> {
                        hideProgressBar()
                        val message = state.getMessage()
                        if (message != null) {
                            showError(message)
                        }
                    }
                    is AuthState.Loading -> {
                        showProgressBar()
                    }
                    is AuthState.Rest -> {
                        hideProgressBar()
                    }
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
        requireView().snack(message, R.color.logout_red)
    }

    private fun authorizeToken(token: String?) {

        val uri = Uri.parse("https://www.themoviedb.org/authenticate/$token")
        val i = Intent(Intent.ACTION_VIEW, uri)
        startActivityForResult(i, REQUEST_CODE_TOKEN_AUTH)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_TOKEN_AUTH) {
            viewModel.setState(AuthState.TokenAuthorized(viewModel.token))
        }
    }

    fun authorizeRequestToken() {
//        val action =
//            RegisterFragmentDirections.actionRegisterFragmentToVerifyTokenFragment(viewModel.requestToken!!)
//        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}
