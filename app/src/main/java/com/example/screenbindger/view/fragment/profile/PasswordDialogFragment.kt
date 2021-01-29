package com.example.screenbindger.view.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentPasswordDialogBinding

class PasswordDialogFragment : DialogFragment() {

    private var _binding: FragmentPasswordDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listener: ChangePasswordListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)
        return view
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentPasswordDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initOnClick()
    }

    private fun initOnClick() {
        with(binding) {
            btnConfirm.setOnClickListener {
                val oldPass = etOldPassword.text.toString()
                val newPass = etNewPassword.text.toString()
                val newPassConfirmed = etNewPasswordConfirm.text.toString()
                listener.validatePassword(oldPass, newPass, newPassConfirmed)
                dialog?.dismiss()
            }

            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = targetFragment as ChangePasswordListener
        } catch (e: ClassCastException) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface ChangePasswordListener {
        fun validatePassword(oldPassword: String, newPassword: String, confirmNewPassword: String)
    }


}