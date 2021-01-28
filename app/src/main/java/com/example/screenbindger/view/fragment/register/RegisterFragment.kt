package com.example.screenbindger.view.fragment.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentRegisterBinding
import com.example.screenbindger.db.remote.service.user.UserActionState
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.state.State
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
import com.google.android.material.snackbar.Snackbar
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
        Handler(Looper.myLooper()!!).postDelayed({
            binding.progressBar.hide()

            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }, 1000)
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
        viewModel.authStateObservable.value.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Success -> {
                    binding.progressBar.hide()
                    gotoMainActivity()
                }
                is State.Error -> {
                    binding.progressBar.hide()

                    val message = state.exception.message ?: "Unknown error"
                    showError(message)
                }
                is State.Loading -> {
                    binding.progressBar.show()
                }
                is State.Unrequested -> {

                }
            }
        }
    }

    private fun observeUserPersistence() {
        viewModel.userActionStateObservable.value.observe(viewLifecycleOwner) {
            when (it) {
                is UserActionState.Failed -> {
                    showError(it.exception.message ?: "Unknown error occurred.")
                }
                else -> {
                }
            }
        }
    }

    private fun showError(message: String) {
        val snackbarColor =
            ResourcesCompat.getColor(resources, R.color.design_default_color_error, null)
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(snackbarColor)
            .show()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}
