package com.example.screenbindger.view.fragment.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.lifecycle.Observer
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentProfileBinding
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.constants.INTENT_REQUEST_CODE_IMAGE
import com.example.screenbindger.util.extensions.snack
import com.example.screenbindger.view.activity.onboarding.OnboardingActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ProfileFragment : DaggerFragment(), PasswordDialogFragment.ChangePasswordListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        initOnClickListeners()
        observeProfilePicture()
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
                showGallery()
            }
            btnLogout.setOnClickListener {
                showLogoutDialog()
            }
            btnChangePassword.setOnClickListener {
                showPasswordUpdateDialog()
            }
        }
    }

    fun observeProfilePicture() {
        viewModel.userStateObservable.profilePictureObservable.observe(
            viewLifecycleOwner,
            Observer { uri ->
                uri?.let { it ->
                    binding.progressBar.hide()
                    setProfileImage(it)
                }
            })
    }

    private fun showGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, INTENT_REQUEST_CODE_IMAGE)
    }

    private fun showPasswordUpdateDialog() {
        val dialog = PasswordDialogFragment()
        dialog.setTargetFragment(this@ProfileFragment, 1)
        dialog.show(this@ProfileFragment.parentFragmentManager, "Change password dialog")
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
                is ProfileViewState.Editable -> {
                    setUiEditable()
                }
                is ProfileViewState.NotEditable -> {
                    setUiNotEditable()
                }
            }
        })

        viewModel.userStateObservable.value.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ObjectState.Updated -> {
                    showMessage(R.string.message_update_success, R.color.green)
                }
                is ObjectState.Error -> {
                    val message = it.exception.message ?: "Unknown error"
                    requireView().snack(message, R.color.design_default_color_error)
                }
                is ObjectState.Created -> {
                    binding.invalidateAll()
                }
                else -> {
                }
            }
        })
    }


    fun prepareFabForSaving() {
        with(binding) {
            fabAddPicture.apply {
                isEnabled = true
                visibility = View.VISIBLE
            }
            fabUpdate.apply {
                setIconAndColor(R.drawable.ic_save_outlined_black_24, R.color.blue)
            }
        }
    }

    fun prepareFabForUpdate() {
        with(binding) {
            fabAddPicture.apply {
                isEnabled = false
                visibility = View.GONE
            }
            fabUpdate.setIconAndColor(
                R.drawable.ic_pencil_black_24,
                R.color.orange
            )
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


    fun viewsEnabled(b: Boolean) {
        with(binding) {
            tilUserFullName.isEnabled = b
            tilUserDateOfBirth.isEnabled = b
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == INTENT_REQUEST_CODE_IMAGE) {
            data?.data?.let { uri ->
                viewModel.uploadImage(uri)
            } ?: showMessage(R.string.message_load_image_from_gallery_error, R.color.logout_red)
        }
    }

    private fun setProfileImage(uri: Uri) {
        binding.ivUserImage.setImageDrawable(null)
        binding.ivUserImage.setUri(uri)
        binding.invalidateAll()
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setUiNotEditable()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    fun showMessage(stringResId: Int, colorResId: Int) {
        requireView().snack(stringResId, colorResId)
    }

}