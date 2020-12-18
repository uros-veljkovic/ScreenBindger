package com.example.screenbindger.view.fragment.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentProfileBinding
import com.example.screenbindger.util.constants.INTENT_REQUEST_CODE_IMAGE
import com.example.screenbindger.view.activity.main.MainActivity
import com.example.screenbindger.view.activity.onboarding.OnboardingActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val viewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        initOnClickListeners()
        observeUpdates()
        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun initOnClickListeners() {
        binding.apply {
            btnAddPicture.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, INTENT_REQUEST_CODE_IMAGE)
            }
            btnLogout.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.logout))
                    .setMessage(resources.getString(R.string.message_logout))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->

                    }
                    .setPositiveButton(resources.getString(R.string.logout)) { dialog, itemSelectedIndex ->
                        viewModel?.logout()
                        gotoOnboardingActivity()
                    }
                    .show()
            }
        }
    }

    private fun gotoOnboardingActivity(){
        startActivity(Intent(requireActivity(), OnboardingActivity::class.java))
        requireActivity().finish()
    }

    private fun observeUpdates(){
        observeDateOfBirthChange()
        observeUpdateTrigger()
    }

    private fun observeDateOfBirthChange() {
        binding.etUserDateOfBirth.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.etUserDateOfBirth.showDatePicker(parentFragmentManager)
        }
    }

    private fun observeUpdateTrigger() {
        viewModel.updated.observe(viewLifecycleOwner, Observer { updated ->
            if (updated) {
                val snackbarColor = ResourcesCompat.getColor(resources, R.color.green, null)
                Snackbar.make(
                    requireView(),
                    getString(R.string.message_updated_profile),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(snackbarColor).show()
                viewModel.updated.postValue(false)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == INTENT_REQUEST_CODE_IMAGE) {
            val imageUri = data?.data
            viewModel.setUserImage(imageUri)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


}