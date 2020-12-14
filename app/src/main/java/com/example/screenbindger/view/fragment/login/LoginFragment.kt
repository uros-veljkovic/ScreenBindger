package com.example.screenbindger.view.fragment.login

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
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentLoginBinding
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = bind(inflater, container)
        observeFieldValidation()
        initOnClickListeners()
        return view
    }

    fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    fun observeFieldValidation() {
        binding.apply {
            btnLogin.enableWhenUsing(FieldValidator()) {
                etUserEmail.isNotEmpty()
                etUserPassword.isNotEmpty()

            }
        }
    }

    fun initOnClickListeners() {
        binding.apply {
            tvHere.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        observeLogin()
    }

    fun observeLogin() {
        viewModel.loginTrigger.observe(viewLifecycleOwner, Observer { isLoggedIn ->
            isLoggedIn?.let {
                if (isLoggedIn == true) {
                    showSuccessMessage()
                    gotoMainActivity()
                } else {
                    showFailMessage()
                }
            }
        })
    }


    private fun showFailMessage() {
        Toast.makeText(
            requireActivity(),
            getString(R.string.login_fail_message),
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun showSuccessMessage() {
        Toast.makeText(
            requireActivity(),
            getString(R.string.login_success_message),
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun gotoMainActivity() {
        binding.progressBar.show()
        Handler(Looper.myLooper()!!).postDelayed({
            binding.progressBar.hide()

            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }, 1500)
    }

}