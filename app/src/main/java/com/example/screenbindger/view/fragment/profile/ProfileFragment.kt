package com.example.screenbindger.view.fragment.profile

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentProfileBinding
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.constants.INTENT_REQUEST_CODE_IMAGE
import com.example.screenbindger.util.state.State
import com.example.screenbindger.view.activity.onboarding.OnboardingActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ProfileFragment : DaggerFragment(), PasswordDialogFragment.ChangePasswordListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        initOnClickListeners()
        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun initOnClickListeners() {
        binding.apply {
            fabAddPicture.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, INTENT_REQUEST_CODE_IMAGE)
            }
            btnLogout.setOnClickListener {
                showLogoutDialog()

            }
            btnChangePassword.setOnClickListener {
                val dialog = PasswordDialogFragment()
                dialog.setTargetFragment(this@ProfileFragment, 1)
                dialog.show(this@ProfileFragment.parentFragmentManager, "Change password dialog")
            }
        }
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.Widget_ScreenBindger_MaterialAlertDialog
        )
            .setTitle(resources.getString(R.string.logout))
            .setMessage(resources.getString(R.string.message_logout))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->

            }
            .setPositiveButton(resources.getString(R.string.logout)) { dialog, itemSelectedIndex ->
                gotoOnboardingActivity()
            }
            .show()
    }

    private fun gotoOnboardingActivity() {
        startActivity(Intent(requireActivity(), OnboardingActivity::class.java))
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()

        observeUpdates()
    }

    private fun observeUpdates() {
        observeDateOfBirthChange()
        observeFiledUpdate()
    }

    private fun observeDateOfBirthChange() {
        binding.etUserDateOfBirth.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.etUserDateOfBirth.showDatePicker(parentFragmentManager)
        }
    }

    private fun observeFiledUpdate() {
        viewModel.fragmentStateObservable.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is FragmentState.Editable -> {
                    setUiEditable()
                }
                is FragmentState.NotEditable -> {
                    setUiNotEditable()
                }
                is FragmentState.UpdateSuccess -> {
                    showMessage(R.string.message_updated_profile_success, R.color.green)
                }
                is FragmentState.UpdateFail -> {
                    showMessage(R.string.message_updated_profile_fail, R.color.logout_red)
                }
            }
        })

        viewModel.userStateObservable.value.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ObjectState.Updated -> {
                    showMessage(R.string.message_update_password_success, R.color.green)
                }
                is ObjectState.Error -> {
                    showMessage(R.string.message_update_password_fail, R.color.green)
                }
                else -> {
                }
            }
        })
    }

    fun prepareFabForSaving() {
        with(binding.fabAddPicture) {
            isEnabled = true
            visibility = View.VISIBLE
            val color = ContextCompat.getColor(context, R.color.green)
            setBackgroundColor(color)
        }
        with(binding.fabUpdate) {
            val color = ContextCompat.getColor(context, R.color.blue)
            backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    fun setUiEditable() {
        binding.btnChangePassword.visibility = View.VISIBLE
        viewsEnabled(true)
        prepareFabForSaving()
    }

    fun setUiNotEditable() {
        binding.btnChangePassword.visibility = View.GONE
        viewsEnabled(false)
        prepareFabForUpdate()
    }

    fun prepareFabForUpdate() {
        with(binding.fabAddPicture) {
            isEnabled = false
            visibility = View.GONE
        }
        with(binding.fabUpdate) {
            setImageResource(R.drawable.ic_pencil_black_24)
            val color = ContextCompat.getColor(context, R.color.orange)
            backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    fun viewsEnabled(b: Boolean) {
        with(binding) {
            tilUserFullName.isEnabled = b
            tilUserDateOfBirth.isEnabled = b
        }
    }

    fun showMessage(stringResId: Int, colorResId: Int) {
        Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG)
            .setActionTextColor(colorResId).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == INTENT_REQUEST_CODE_IMAGE) {
            val imageUri = data?.data
//            viewModel.setUserImage(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun validatePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        when {
            oldPassword == newPassword -> {
                showMessage(R.string.message_old_new_password_same, R.color.logout_red)
                return
            }
            (newPassword == confirmNewPassword).not() -> {
                showMessage(R.string.message_passwords_not_match, R.color.logout_red)
                return
            }
            newPassword.isEmpty() -> {
                showMessage(R.string.message_new_password_empty, R.color.logout_red)
                return
            }
            else -> {
                viewModel.changePassword(newPassword)
            }
        }
    }

}