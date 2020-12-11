package com.example.screenbindger.view.fragment.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegisterBinding.inflate(layoutInflater)
        initOnClickListeners()

        return binding.root
    }

    fun initOnClickListeners(){
        binding.etUserDateOfBirth.setOnFocusChangeListener { _, gotFocus ->
            if(gotFocus){
                createDatePicker()
            }
        }

    }

    private fun createDatePicker(){
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Select your date of birth")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            etUserDateOfBirth.setText(datePicker.headerText)
            binding.etUserDateOfBirth.clearFocus()
        }

        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
    }

}

inline fun EditText.onFocusChange(crossinline hasFocus: (Boolean) -> Unit) {
    setOnFocusChangeListener(View.OnFocusChangeListener { view, gotFocus -> })
}