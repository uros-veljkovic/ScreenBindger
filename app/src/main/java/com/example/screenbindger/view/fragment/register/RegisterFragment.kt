package com.example.screenbindger.view.fragment.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.screenbindger.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = bind(inflater, container)
        initOnClickListeners()

        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun initOnClickListeners() {
        binding.etUserDateOfBirth.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                etUserDateOfBirth.showDatePicker(parentFragmentManager)
        }
    }
}

inline fun EditText.onFocusChange(crossinline hasFocus: (Boolean) -> Unit) {
    setOnFocusChangeListener(View.OnFocusChangeListener { view, gotFocus -> })
}
