package com.example.screenbindger.view.fragment.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentRegisterBinding
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snack
import com.example.screenbindger.util.extensions.startActivityWithDelay
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
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
        binding.progressBar.hide()
        startActivityWithDelay(MainActivity(), 1000)

    }

    private fun gotoLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onResume() {
        super.onResume()

        observeRegistration()
        observeUserPersistence()
    }

    private fun observeRegistration() {
        viewModel.apply {
            authStateObservable.value.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is AuthState.FirebaseAuthSuccess -> {
                        getRequestToken()
                    }
                    is AuthState.TokenGathered -> {
                        authorizeRequestToken()
                    }
                    is AuthState.TokenAuthorized -> {
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
                        binding.progressBar.show()
                    }
                    is AuthState.Rest -> {
                    }
                }
            })
        }
    }

    private fun getRequestToken() {
        TODO("Not yet implemented")
    }

    private fun authorizeRequestToken() {
        TODO("Not yet implemented")
    }

    private fun createSession() {
        TODO("Not yet implemented")
    }

    private fun observeUserPersistence() {
        viewModel.userStateObservable.value.observe(viewLifecycleOwner) {
            when (it) {
                is ObjectState.Error -> {
                    showError(it.exception.message ?: "Unknown error occurred.")
                }
                is ObjectState.Created -> {
                    gotoMainActivity()
                }
                else -> {
                }
            }
        }
    }

    private fun showError(message: String) {
        requireView().snack(message, R.color.logout_red)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}
