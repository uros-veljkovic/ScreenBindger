package com.example.screenbindger.view.fragment.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentRegisterBinding
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()


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
        binding.btnRegister.setOnClickListener {
            progressBar.show()
            viewModel.register()
            gotoMainActivity()
        }

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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}
